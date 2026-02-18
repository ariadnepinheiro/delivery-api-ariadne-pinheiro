package com.deliverytech.delivery.dto.requests;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados para criação de um pedido.")
public class PedidoDTO {
    
    @Schema(description = "Endereço de entrega do pedido.", example = "Rua das Flores, 123, Bairro Jardim, Cidade XYZ")
    @NotBlank(message = "O campo 'endereço' é obrigatório.")
    private String enderecoEntrega;

    @Schema(description = "ID do cliente que fez o pedido.", example = "1")
    @NotNull(message = "O campo 'cliente' é obrigatório.")
    private Long clienteId;

    @Schema(description = "ID do restaurante onde o pedido foi feito.", example = "1")
    @NotNull(message = "O campo 'restaurante' é obrigatório.")
    private Long restauranteId;

    @Schema(description = "Lista de itens do pedido.")
    @Valid
    @NotNull(message = "O campo 'itens' é obrigatório.")
    @Size(min = 1, message = "O pedido deve ter pelo menos um item.")
    private List<ItemPedidoDTO> itens;

}