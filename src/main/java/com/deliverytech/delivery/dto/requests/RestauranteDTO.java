package com.deliverytech.delivery.dto.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestauranteDTO {
    
    @NotNull(message = "O nome do restaurante é obrigatório.")
    @NotBlank(message = "O nome do restaurante não pode estar em branco.")
    @Size(max = 100, message = "O nome do restaurante deve ter no máximo 100 caracteres.")
    private String nome;

    @NotBlank(message = "O endereço do restaurante é obrigatório.")
    @Size(max = 255, message = "O endereço do restaurante deve ter no máximo 255 caracteres.")
    private String endereco;

    @NotBlank(message = "O telefone do restaurante é obrigatório.")
    @Size(max = 15, message = "O telefone do restaurante deve ter no máximo 15 caracteres.")
    private String telefone;

    @NotBlank(message = "A categoria do restaurante é obrigatória.")
    @Size(max = 50, message = "A categoria do restaurante deve ter no máximo 50 caracteres.")
    private String categoria;

    @NotNull(message = "A taxa de entrega do restaurante é obrigatória.")
    private BigDecimal taxaEntrega;
    
}