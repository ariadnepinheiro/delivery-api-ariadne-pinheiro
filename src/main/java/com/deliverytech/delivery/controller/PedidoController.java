package com.deliverytech.delivery.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.enums.StatusPedidos;
import com.deliverytech.delivery.model.Pedido;
import com.deliverytech.delivery.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;    

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/criar")
    public Pedido criarPedido(@RequestParam Long clienteId, @RequestParam Long restauranteId) {
        return pedidoService.criarPedido(clienteId, restauranteId);
    }

    @PutMapping("/{id}/status")
    public Pedido atualizarStatusPedido(@PathVariable Long id, @RequestParam StatusPedidos status) {
        return pedidoService.atualizarStatus(id, status);
    }

    @GetMapping("/cliente/{id}")
    public List<Pedido> listarPedidosPorCliente(@PathVariable Long id) {
        return pedidoService.listarPorCliente(id);
    }
}
