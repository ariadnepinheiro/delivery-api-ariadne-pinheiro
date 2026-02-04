package com.deliverytech.delivery.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ItemPedidoDTO {
    @NotNull
    @Min(1)
    @Positive
    private Integer quantidade;

    @NotNull
    private Long produtoId;
}
