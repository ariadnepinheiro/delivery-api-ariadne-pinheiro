package com.deliverytech.delivery.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.dto.responses.ProdutoResponseDTO;
import com.deliverytech.delivery.model.Produto;
import com.deliverytech.delivery.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoService produtoService;
    
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return produtoService.buscarProdutoPorId(id);
    }

    @PostMapping("/{restauranteId}")
    public ResponseEntity<Produto> cadastrarProduto(@PathVariable Long restauranteId, @RequestBody Produto produto) {
        return ResponseEntity.status(201).body(produtoService.cadastrarProduto(restauranteId, produto));
    }

    @GetMapping("/restaurante/{restauranteId}")
    public List<Produto> listarProdutosDoRestaurante(@PathVariable Long restauranteId) {
        return produtoService.listarProdutosPorRestaurante(restauranteId);
    }
}
