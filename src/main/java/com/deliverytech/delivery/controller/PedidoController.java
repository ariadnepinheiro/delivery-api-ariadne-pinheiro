package com.deliverytech.delivery.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.dto.requests.PedidoDTO;
import com.deliverytech.delivery.dto.responses.ApiResponse;
import com.deliverytech.delivery.dto.responses.PagedResponse;
import com.deliverytech.delivery.dto.responses.PedidoResponseDTO;
import com.deliverytech.delivery.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/pedidos", produces = "application/json")
@Tag(name = "Pedidos", description = "Endpoints relacionadas a pedidos.")
@CrossOrigin(origins = "*")
public class PedidoController {
    private final PedidoService pedidoService;    

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Criar um novo pedido")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "201", 
        description = "Pedido criado com sucesso"
    )

    @PostMapping
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> criarPedido(@RequestBody @Valid PedidoDTO pedidoDTO) {
        return ResponseEntity.ok(new ApiResponse<>(pedidoService.criarPedido(pedidoDTO)));
    }

    @Operation(summary = "Listar histórico de pedidos do cliente (paginado).")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<PagedResponse<PedidoResponseDTO>> listarItensPorCliente(
        @PathVariable Long clienteId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size){
        
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(new PagedResponse<>(pedidoService.listarItensPorCliente(clienteId, pageable)));
    }

    @Operation(summary = "Confirmar um pedido (Aceite do restaurante).")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {    
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Pedido confirmado com sucesso."
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Pedido não está em estado PENDENTE."
        )
    })

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> confirmarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(pedidoService.confirmarPedido(id)));
    }

    @Operation(summary = "Avançar o status do pedido (Fluxo: CONFIRMADO -> PREPARANDO -> ENTREGA).")
    @PatchMapping("/{id}/status/avancar")
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> atualizarStatusPedido(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(pedidoService.atualizarStatus(id)));
    }

    @Operation(summary = "Cancelar um pedido (Disponível apenas para pedidos PENDENTES ou CONFIRMADOS).")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> cancelarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(pedidoService.cancelarPedido(id)));
    }
}
