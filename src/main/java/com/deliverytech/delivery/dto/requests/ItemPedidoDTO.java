package com.deliverytech.delivery.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados para representar um item específico de um pedido.")
public class ItemPedidoDTO {

    @Schema(description = "Quantidade do produto no pedido.", example = "2")
    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade deve ser maior que zero.")
    @Positive(message = "A quantidade deve ser um número positivo.")
    private Integer quantidade;

    @Schema(description = "ID do produto desejado.", example = "10")
    @NotNull(message = "O ID do produto é obrigatório.")
    private Long produtoId;
}
