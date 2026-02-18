package com.deliverytech.delivery.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.dto.responses.ItemPedidoResponseDTO;
import com.deliverytech.delivery.repository.ItemPedidoRepository;

@Service
public class ItemPedidoService {
    private final ItemPedidoRepository repository;
    private final ModelMapper modelMapper;

    public ItemPedidoService(ItemPedidoRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public Page<ItemPedidoResponseDTO> listarPorPedido(Long pedidoId, Pageable pageable){
        return repository.findByPedidoId(pedidoId, pageable).map(item -> modelMapper.map(item, ItemPedidoResponseDTO.class));
    }
}