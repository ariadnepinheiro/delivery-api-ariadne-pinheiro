package com.deliverytech.delivery.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.dto.TotalVendasPorRestauranteDTO;
import com.deliverytech.delivery.enums.StatusPedidos;
import com.deliverytech.delivery.model.Pedido;


@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByStatus(StatusPedidos status);

    @Query("""
            SELECT p FROM Pedido p 
            WHERE p.dataPedido BETWEEN :inicio AND :fim
        """)
        List<Pedido> findByDateTime(
            @Param("inicio") LocalDateTime inicio, 
            @Param("fim") LocalDateTime fim
    );

    @Query("""
            select com.deliverytech.delivery.dto.TotalVendasPorRestauranteDTO(
            r.NOME,
            coalesce(sum(ip.SUBTOTAL), 0) as total_por_restaurante
            )
            from RESTAURANTES r
            join PEDIDOS p on p.RESTAURANTE_ID = r.ID 
            join ITENS_PEDIDO ip on ip.PEDIDO_ID = p.ID
            group by r.NOME;
        """)
        List<TotalVendasPorRestauranteDTO> totalVendasPorRestaurante();
}
