package com.deliverytech.delivery.dto.requests;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados para representar um restaurante.")
public class RestauranteDTO {

    @Schema(description = "Nome do restaurante.", example = "Pizzaria do Zé")
    @NotNull(message = "O nome do restaurante é obrigatório.")
    @NotBlank(message = "O nome do restaurante não pode estar em branco.")
    @Size(max = 100, message = "O nome do restaurante deve ter no máximo 100 caracteres.")
    private String nome;

    @Schema(description = "Endereço do restaurante.", example = "Rua das Flores, 123, Bairro Jardim, Cidade XYZ")
    @Size(max = 255, message = "O endereço do restaurante deve ter no máximo 255 caracteres.")
    private String endereco;

    @Schema(description = "Telefone do restaurante.", example = "(11) 98765-4321")
    @NotBlank(message = "O telefone do restaurante é obrigatório.")
    @Size(max = 15, message = "O telefone do restaurante deve ter no máximo 15 caracteres.")
    @Pattern(
        regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", 
        message = "O telefone do restaurante deve estar no formato (XX) XXXXX-XXXX ou (XX) XXXX-XXXX."
    )
    private String telefone;

    @Schema(description = "Categoria do restaurante.", example = "Pizzaria")
    @NotBlank(message = "A categoria do restaurante é obrigatória.")
    @Size(max = 50, message = "A categoria do restaurante deve ter no máximo 50 caracteres.")
    private String categoria;

    @Schema(description = "Taxa de entrega do restaurante.", example = "5,00")
    @NotNull(message = "A taxa de entrega do restaurante é obrigatória.")
    private BigDecimal taxaEntrega;
    
}