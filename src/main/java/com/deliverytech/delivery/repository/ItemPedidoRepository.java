package com.deliverytech.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.delivery.model.ItemPedido;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long>{
    List<ItemPedido> findByPedidoId(Long pedidoId);
    List<ItemPedido> findByProdutoId(Long produtoId);    
}
