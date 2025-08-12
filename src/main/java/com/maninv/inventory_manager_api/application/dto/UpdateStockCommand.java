package com.maninv.inventory_manager_api.application.dto;

public record UpdateStockCommand(
        String storeId,
        String productId,
        Integer quantityChange
) {}
