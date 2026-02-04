package com.deliverytech.delivery.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.dto.requests.ProdutoDTO;
import com.deliverytech.delivery.dto.responses.ProdutoResponseDTO;
import com.deliverytech.delivery.service.ProdutoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoService produtoService;
    
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    /*@GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return produtoService.buscarProdutoPorId(id);
    }*/

    @PostMapping("/restaurante/{restauranteId}")
    public ResponseEntity<ProdutoResponseDTO> cadastrarProduto(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoDTO produto) {
        ProdutoResponseDTO produtoCadastrado = produtoService.cadastrarProduto(restauranteId, produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCadastrado);
    }

    @GetMapping("/restaurante/{restauranteId}")
    public List<ProdutoResponseDTO> listarProdutosDoRestaurante(@PathVariable Long restauranteId) {
        return produtoService.listarProdutosPorRestaurante(restauranteId);
    }

    @PatchMapping("/{produtoId}/toggle-disponibilidade")
    public ResponseEntity<ProdutoResponseDTO> toggleDisponibilidade(@PathVariable Long produtoId) {
        ProdutoResponseDTO produtoAtualizado = produtoService.toggleDisponibilidade(produtoId);
        return ResponseEntity.ok(produtoAtualizado);
    }
}
