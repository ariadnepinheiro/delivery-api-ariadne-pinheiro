package com.deliverytech.delivery.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.dto.TotalVendasPorRestauranteDTO;
import com.deliverytech.delivery.enums.StatusPedidos;
import com.deliverytech.delivery.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query(value = """
            SELECT DISTINCT p 
            FROM Pedido p 
                JOIN FETCH p.cliente
                JOIN FETCH p.restaurante
                LEFT JOIN FETCH p.itens i
                LEFT JOIN FETCH i.produto
            WHERE p.cliente.id = :clienteId
        """,
        countQuery = "SELECT COUNT(DISTINCT p) FROM Pedido p WHERE p.cliente.id = :clienteId")
        
        Page<Pedido> buscarItensPorCliente(@Param("clienteId") Long clienteId, Pageable pageable);
        Page<Pedido> findByStatus(StatusPedidos status);

    @Query("""
            SELECT p
            FROM Pedido p
            WHERE p.dataPedido BETWEEN :dataInicio AND :dataFim
        """)
        List<Pedido> findByDateTime(
            @Param("dataInicio") LocalDateTime dataInicio, 
            @Param("dataFim") LocalDateTime dataFim
        );

    @Query("""
            SELECT NEW com.deliverytech.delivery.dto.TotalVendasPorRestauranteDTO(
                r.nome, 
                coalesce(sum(ip.subtotal), 0)
            )
            FROM Pedido p
                JOIN p.restaurante r
                LEFT JOIN p.itens ip
            GROUP BY r.nome
        """)
        List<TotalVendasPorRestauranteDTO> totalVendasPorRestaurante();
        
    @Query(value="""
            SELECT c.nome AS cliente, COUNT(p.id) AS total_pedidos
            FROM pedidos p 
                    JOIN clientes c ON c.id = p.cliente_id
            GROUP BY c.nome
            ORDER BY total_pedidos DESC
        """, nativeQuery = true )
        List<Object[]> rankingClientes();
}
