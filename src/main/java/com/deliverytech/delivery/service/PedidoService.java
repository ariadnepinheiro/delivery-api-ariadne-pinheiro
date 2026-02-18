package com.deliverytech.delivery.service;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.dto.requests.ItemPedidoDTO;
import com.deliverytech.delivery.dto.requests.PedidoDTO;
import com.deliverytech.delivery.dto.responses.PedidoResponseDTO;
import com.deliverytech.delivery.enums.StatusPedidos;
import com.deliverytech.delivery.exceptions.BusinessException;
import com.deliverytech.delivery.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.model.ItemPedido;
import com.deliverytech.delivery.model.Pedido;
import com.deliverytech.delivery.model.Produto;
import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.ItemPedidoRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;

@Service
public class PedidoService {
    @Autowired
    private final PedidoRepository pedidoRepository;

    @Autowired
    private final ClienteRepository clienteRepository;

    @Autowired
    private final RestauranteRepository restauranteRepository;

    @Autowired
    private final ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private final ProdutoRepository produtoRepository;

    private final ModelMapper modelMapper;

    private PedidoResponseDTO toResponseDTO(Pedido pedido){
        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository,
            RestauranteRepository restauranteRepository, ItemPedidoRepository itemPedidoRepository,
            ProdutoRepository produtoRepository, ModelMapper modelMapper) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.restauranteRepository = restauranteRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.produtoRepository = produtoRepository;
        this.modelMapper = modelMapper; 
    }

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoDTO pedidoDTO){
        Cliente cliente = clienteRepository.findById(pedidoDTO.getClienteId())
        .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));

        if(!cliente.getAtivo()){
            throw new BusinessException("Cliente inativo não pode criar pedidos.");
        }

        Restaurante restaurante = restauranteRepository.findById(pedidoDTO.getRestauranteId())
        .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado."));

        if(!restaurante.getAtivo()){
            throw new BusinessException("Restaurante inativo não pode receber pedidos.");
        }
    
        Pedido entradaPedido = new Pedido();        
        entradaPedido.setCliente(cliente);
        entradaPedido.setRestaurante(restaurante);
        entradaPedido.setStatus(StatusPedidos.PENDENTE);
        entradaPedido.setEnderecoEntrega(pedidoDTO.getEnderecoEntrega());

        BigDecimal total = BigDecimal.ZERO;

        for(ItemPedidoDTO itemDTO : pedidoDTO.getItens()){
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

            if(!produto.getDisponivel()){
                throw new BusinessException("Produto " + produto.getNome() + " não está disponível no momento.");
            }

            ItemPedido item = new ItemPedido();
            item.setPedido(entradaPedido);
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());

            BigDecimal subtotal = produto.getPreco()
                .multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));
            item.setSubtotal(subtotal);
            itemPedidoRepository.save(item);

            entradaPedido.getItens().add(item);
            total = total.add(subtotal);
        }
        
        entradaPedido.setValorTotal(total);
        Pedido pedidoSalvo = pedidoRepository.save(entradaPedido);
        return toResponseDTO(pedidoSalvo);

    }
    
    @Transactional
    public PedidoResponseDTO confirmarPedido(Long pedidoId){
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));

        if(pedido.getStatus() != StatusPedidos.PENDENTE){
            throw new BusinessException("Apenas pedidos PENDENTES podem ser confirmados.");
        }

        pedido.setStatus(StatusPedidos.CONFIRMADO);
        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        return toResponseDTO(pedidoAtualizado);
    }

    @Transactional
    public PedidoResponseDTO atualizarStatus(Long pedidoId){
        Pedido pedido = pedidoRepository.findById(pedidoId)
        .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));

        StatusPedidos statusAtual = pedido.getStatus();

        switch(statusAtual){
            case CONFIRMADO:
                pedido.setStatus(StatusPedidos.PREPARANDO);
                break;
            case PREPARANDO:
                pedido.setStatus(StatusPedidos.SAIU_PARA_ENTREGA);
                break;
            case SAIU_PARA_ENTREGA:
                pedido.setStatus(StatusPedidos.ENTREGUE);
                break;
            case CANCELADO, ENTREGUE:
                throw new BusinessException("Status do Pedido não pode mais ser avançado.");
            default:
                throw new BusinessException("Status de pedido inválido para avanço.");
        }
        
        return toResponseDTO(pedido);
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponseDTO> listarItensPorCliente(Long clienteId, Pageable pageable){
        
        if(!clienteRepository.existsById(clienteId)){
            throw new EntityNotFoundException("Cliente não encontrado.");
        }
        
        return pedidoRepository.buscarItensPorCliente(clienteId, pageable)
        .map(this::toResponseDTO);
    }

    @Transactional
    public PedidoResponseDTO cancelarPedido(Long pedidoId){
        Pedido pedido = pedidoRepository.findById(pedidoId)
        .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));

        if(pedido.getStatus() == StatusPedidos.ENTREGUE){
            throw new BusinessException("Pedidos ENTREGUES não podem ser cancelados.");
        }

        pedido.setStatus(StatusPedidos.CANCELADO);
        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        return toResponseDTO(pedidoAtualizado);
    }

    /*public List<PedidoResponseDTO> listarPorCliente(Long clienteId, boolean toDTO){
        List<Pedido> pedidos = pedidoRepository.buscarItensPorCliente(clienteId);
        if(toDTO){
            return pedidos.stream()
            .map(this::toResponseDTO)
            .toList();
        }
        return pedidos.stream()
            .map(this::toResponseDTO)
            .toList();
    }

    public ItemPedido adicionarItem(Long pedidoId, Long produtoId, Integer quantidade){
        Pedido pedido = pedidoRepository.findById(pedidoId)
        .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));

        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());

        BigDecimal subtotal = produto.getPreco()
            .multiply(BigDecimal.valueOf(quantidade));
        item.setSubtotal(subtotal);
        itemPedidoRepository.save(item);

        pedido.setValorTotal(pedido.getValorTotal().add(subtotal));
        pedidoRepository.save(pedido);

        return item;
    }*/
}
