package com.deliverytech.delivery.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.service.ClienteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<Cliente> cadastrarCliente(@RequestBody Cliente cliente) {
        return ResponseEntity.status(201).body(clienteService.cadastrarCliente(cliente));
    }

    @GetMapping("/listar")
    public List<Cliente> listarClientes() {
        return clienteService.listarAtivos();
    }
    
    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Cliente atualizarCliente(@PathVariable Long id, @RequestBody Cliente novoCliente) {
        return clienteService.atualizarCliente(id, novoCliente);
    }

    @PutMapping("/desativar/{id}")
    public ResponseEntity<Void> desativarCliente(@PathVariable Long id) {
        clienteService.desativarCliente(id);
        return ResponseEntity.noContent().build();
    }

}
