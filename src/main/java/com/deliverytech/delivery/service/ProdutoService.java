package com.deliverytech.delivery.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository = null;
    private final RestauranteRepository restauranteRepository = null;
    private final ModelMapper modelMapper = new ModelMapper();

    private ProdutoResponseDTO returnResponseDTO(Produto produto) {
        ProdutoResponseDTO produtoDTO = modelMapper.map(produto, ProdutoResponseDTO.class);
        if(produto.getRestaurante() != null){
            produtoDTO.setRestauranteId(produto.getRestaurante().getId());
        }
        return produtoDTO;
    }

    /*public ProdutoService(ProdutoRepository produtoRepository, RestauranteRepository restauranteRepository, ModelMapper modelMapper) {
        this.produtoRepository = produtoRepository;
        this.restauranteRepository = restauranteRepository;
        this.modelMapper = modelMapper;
    }*/

    @Transactional
    public ProdutoResponseDTO cadastrarProduto(Long restauranteId, ProdutoDTO produto) {

        Restaurante restaurante = restauranteRepository.findById(restauranteId)
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não localizado."));

        if (!restaurante.getAtivo()) {
            throw new BusinessException("Restaurante inativo. Não é possível cadastrar produtos.");
        }

        Produto novoProduto = modelMapper.map(produto, Produto.class);
        novoProduto.setDisponivel(true);
        novoProduto.setRestaurante(restaurante);

        return returnResponseDTO(produtoRepository.save(novoProduto));
    }

    public Page<ProdutoResponseDTO> listarProdutosPorRestaurante(Long restauranteId, Pageable pageable) {
        if(!restauranteRepository.existsById(restauranteId)) {
            throw new EntityNotFoundException("Restaurante não localizado.");
        }
        
        return produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId, pageable)
            .map(this::returnResponseDTO);
    }

    public ProdutoResponseDTO buscarProdutoPorId(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new EntityNotFoundException("Produto não localizado."));
        return returnResponseDTO(produto);
    }

    @Transactional
    public ProdutoResponseDTO toggleDisponibilidade(Long produtoId){
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new EntityNotFoundException("Produto não localizado."));
        produto.setDisponivel(!produto.getDisponivel());

        Produto produtoSalvo = produtoRepository.save(produto);
        return returnResponseDTO(produtoSalvo);
    }

}