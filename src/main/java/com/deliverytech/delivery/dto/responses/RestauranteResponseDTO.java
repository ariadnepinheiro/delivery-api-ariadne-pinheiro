package com.deliverytech.delivery.dto.responses;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
public class RestauranteResponseDTO {

    private String nome;
    private String categoria;
    private String endereco;
    private String telefone;
    private BigDecimal avaliacao;
    private BigDecimal taxaEntrega;
    private Boolean ativo;

}