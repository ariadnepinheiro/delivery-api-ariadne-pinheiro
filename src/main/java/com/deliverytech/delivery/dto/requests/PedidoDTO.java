package com.deliverytech.delivery.dto.requests;

import java.util.List;

import com.deliverytech.delivery.model.ItemPedido;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoDTO {
    
    @NotBlank(message = "O campo 'endereco' é obrigatório.")
    private String enderecoEntrega;

    @NotNull(message = "O campo 'clienteId' é obrigatório.")
    private Long clienteId;

    @NotNull(message = "O campo 'restauranteId' é obrigatório.")
    private Long restauranteId;

    @Valid
    @NotNull(message = "O campo 'itens' é obrigatório.")
    private List<ItemPedido> itens;

}