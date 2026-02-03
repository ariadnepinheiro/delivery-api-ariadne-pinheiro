package com.deliverytech.delivery.dto.responses;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
public class ProdutoResponseDTO {
    
    private String nome;
    private String descricao;
    private String categoria;
    private BigDecimal preco;
    private Boolean disponivel;
    private Long restauranteId;

}
