package com.deliverytech.delivery.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery.dto.TotalVendasPorRestauranteDTO;
import com.deliverytech.delivery.repository.PedidoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    private final PedidoRepository respository;
    
    public List<TotalVendasPorRestauranteDTO> totalVendasPorRestaurante(){
        return respository.totalVendasPorRestaurante();
    }
}
