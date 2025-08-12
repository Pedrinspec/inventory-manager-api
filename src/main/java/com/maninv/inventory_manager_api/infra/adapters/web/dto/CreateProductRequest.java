package com.maninv.inventory_manager_api.infra.adapters.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateProductRequest(
        @NotBlank(message = "ProductId cannot be empty.")
        String productId,

        @NotBlank(message = "Product description cannot be empty.")
        String description,

        @NotNull
        @Size(min = 1, message = "There must be at least one beginning inventory item.")
        List<InitialStockItem> initialStock
) {
    public record InitialStockItem(
            @NotBlank(message = "Store ID cannot be empty.")
            String storeId,

            @NotNull
            @PositiveOrZero(message = "Initial quantity cannot be negative.")
            Integer quantity
    ) {}
}
