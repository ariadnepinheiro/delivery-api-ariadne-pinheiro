package com.deliverytech.delivery.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.dto.responses.ItemPedidoResponseDTO;
import com.deliverytech.delivery.dto.responses.PagedResponse;
import com.deliverytech.delivery.model.ItemPedido;
import com.deliverytech.delivery.service.ItemPedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value ="/item-pedidos", produces = "application/json")
@Tag(name = "Item do Pedido", description = "Endpoints relacionadas a itens de pedido.")
@CrossOrigin(origins = "*")
public class ItemPedidoController {

    private final ItemPedidoService service;
    
    public ItemPedidoController(ItemPedidoService service) {
        this.service = service;
    }
    
    @Operation(summary = "Listar itens de pedido específico (paginado).")
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<PagedResponse<ItemPedidoResponseDTO>> listarPorPedido(
            @PathVariable Long pedidoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
                
        Pageable pageable = PageRequest.of(page, size);
        var pagedData = service.listarPorPedido(pedidoId, pageable);
        return ResponseEntity.ok(new PagedResponse<>(pagedData));
    }
}
