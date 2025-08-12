package com.maninv.inventory_manager_api.application.dto;

import java.util.List;

public record ProductInventoryResponse(
        String productId,
        String description,
        Integer totalStock,
        List<StoreStock> stockByStore
) {
    public record StoreStock(String storeId, Integer quantity) {}
}
