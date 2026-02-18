package com.deliverytech.delivery.dto.requests;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados para representar um produto.")
public class ProdutoDTO {
    
    @Schema(description = "Identificador único do produto.", example = "Hambúrguer artesanal")
    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(max = 100, message = "O nome do produto deve ter no máximo 100 caracteres.")
    private String nome;

    @Schema(description = "Descrição detalhada do produto.", example = "Hambúrguer artesanal feito com carne de alta qualidade, servido com queijo, alface, tomate e molho especial.")
    @NotBlank(message = "A descrição do produto é obrigatória.")
    @Size(min=5, max=255, message = "A descrição do produto deve ter no mínimo 5 e no máximo 255 caracteres.")
    private String descricao;

    @Schema(description = "Categoria do produto.", example = "Lanches")
    @NotBlank(message = "A categoria do produto é obrigatória.")
    private String categoria;

    @Schema(description = "Preço do produto.", example = "29.90")
    @Positive
    @NotNull(message = "O preço do produto é obrigatório.")
    private BigDecimal preco;
    
}
