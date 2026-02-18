package com.deliverytech.delivery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deliverytech.delivery.model.ItemPedido;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long>{

    @Query(value = """
        SELECT ip FROM ItemPedido ip
        JOIN FETCH ip.pedido
        WHERE ip.pedido.id = :pedidoId
            """,
        countQuery = "SELECT COUNT(ip) FROM ItemPedido ip WHERE ip.pedido.id = :pedidoId")

    Page<ItemPedido> findByPedidoId(@Param("pedidoId") Long pedidoId, Pageable pageable);
    Page<ItemPedido> findByProdutoId(Long produtoId, Pageable pageable);    
}
