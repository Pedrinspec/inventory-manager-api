package com.maninv.inventory_manager_api.application.dto;

import java.util.List;

public record CreateProductCommand(
        String productId,
        String description,
        List<InitialStock> initialStock
) {
    public record InitialStock(String storeId, int quantity) {}
}
