package com.deliverytech.delivery.service;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery.dto.requests.ProdutoDTO;
import com.deliverytech.delivery.dto.responses.ProdutoResponseDTO;
import com.deliverytech.delivery.exceptions.BusinessException;
import com.deliverytech.delivery.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.model.Produto;
import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final RestauranteRepository restauranteRepository;
    private final ModelMapper modelMapper;
    
    public ProdutoService(ProdutoRepository produtoRepository, RestauranteRepository restauranteRepository, ModelMapper modelMapper) {
        this.produtoRepository = produtoRepository;
        this.restauranteRepository = restauranteRepository;
        this.modelMapper = modelMapper;
    }

    private ProdutoResponseDTO returnResponseDTO(Produto produto) {
        ProdutoResponseDTO produtoDTO = modelMapper.map(produto, ProdutoResponseDTO.class);
        if(produto.getRestaurante() != null){
            produtoDTO.setRestauranteId(produto.getRestaurante().getId());
        }
        return produtoDTO;
    }

    @Transactional
    public ProdutoResponseDTO cadastrarProduto(Long restauranteId, ProdutoDTO produto) {
        if(produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O preço do produto deve ser maior que zero.");
        }

        Restaurante restaurante = restauranteRepository.findById(restauranteId)
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não localizado."));

        if (!restaurante.getAtivo()) {
            throw new BusinessException("Restaurante inativo. Não é possível cadastrar produtos.");
        }

        Produto novoProduto = modelMapper.map(produto, Produto.class);
        novoProduto.setDisponivel(true);
        novoProduto.setRestaurante(restaurante);
        Produto produtoSalvo = produtoRepository.save(novoProduto);

        ProdutoResponseDTO produtoResposta = modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);
        return produtoResposta;
    }

    public List<ProdutoResponseDTO> listarProdutosPorRestaurante(Long restauranteId) {
        if(!restauranteRepository.existsById(restauranteId)) {
            throw new EntityNotFoundException("Restaurante não localizado.");
        }
        
        return produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId)
        .stream()
        .map(produto -> {
            ProdutoResponseDTO produtoDTO = modelMapper.map(produto, ProdutoResponseDTO.class);
            return produtoDTO;
        })
        .toList();
    }

    public ProdutoResponseDTO buscarProdutoPorId(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new EntityNotFoundException("Produto não localizado."));
        ProdutoResponseDTO produtoDTO = modelMapper.map(produto, ProdutoResponseDTO.class);
        return produtoDTO;
    }

    public ProdutoResponseDTO toggleDisponibilidade(Long produtoId){
        Produto produto = produtoRepository.findById(produtoId)
        .orElseThrow(() -> new EntityNotFoundException("Produto não localizado."));
        produto.setDisponivel(!produto.getDisponivel());

        Produto produtoSalvo = produtoRepository.save(produto);

        return returnResponseDTO(produtoSalvo);
    }

}