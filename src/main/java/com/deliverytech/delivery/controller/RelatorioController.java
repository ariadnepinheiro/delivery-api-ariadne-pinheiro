package com.deliverytech.delivery.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.dto.TotalVendasPorRestauranteDTO;
import com.deliverytech.delivery.service.RelatorioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/relatorio")
@RequiredArgsConstructor
public class RelatorioController {
    private final RelatorioService service;

    @GetMapping("/total-vendas-por-restaurante")
    public List<TotalVendasPorRestauranteDTO> totalVendasPorRestaurante(){
        return service.totalVendasPorRestaurante();
    }

}