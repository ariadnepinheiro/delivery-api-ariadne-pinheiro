package com.deliverytech.delivery.dto.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoDTO {
    
    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(max = 100, message = "O nome do produto deve ter no máximo 100 caracteres.")
    private String nome;

    @Size(max = 255, message = "A descrição do produto deve ter no máximo 255 caracteres.")
    private String descricao;

    @NotBlank(message = "A categoria do produto é obrigatória.")
    @Size(max = 50, message = "A categoria do produto deve ter no máximo 50 caracteres.")
    private String categoria;

    @NotNull(message = "O preço do produto é obrigatório.")
    @Positive(message = "O preço do produto deve ser um valor positivo.")
    private BigDecimal preco;
    
}
