package com.maninv.inventory_manager_api.infra.adapters.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateProductRequest(
        @NotBlank(message = "O ID do produto não pode ser vazio.")
        String productId,

        @NotBlank(message = "A descrição do produto não pode ser vazia.")
        String description,

        @NotNull
        @Size(min = 1, message = "Deve haver pelo menos um item de estoque inicial.")
        List<InitialStockItem> initialStock
) {
    public record InitialStockItem(
            @NotBlank(message = "O ID da loja não pode ser vazio.")
            String storeId,

            @NotNull
            @PositiveOrZero(message = "A quantidade inicial não pode ser negativa.")
            Integer quantity
    ) {}
}
