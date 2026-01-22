package com.deliverytech.delivery.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery.model.Produto;
import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final RestauranteRepository restauranteRepository;
    
    public ProdutoService(ProdutoRepository produtoRepository, RestauranteRepository restauranteRepository) {
        this.produtoRepository = produtoRepository;
        this.restauranteRepository = restauranteRepository;
    }

    public Produto cadastrarProduto(Long restauranteId, Produto produto) {
        if(produto.getPreco() ==null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não localizado."));
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);
        return produtoRepository.save(produto);
    }

    public List<Produto> listarProdutosPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId);
    }

    public Object adicionarProdutoNoRestaurante(Long restauranteId, Produto produtoRequest) {
        throw new UnsupportedOperationException("Unimplemented method 'adicionarProdutoNoRestaurante'");
    }

}
