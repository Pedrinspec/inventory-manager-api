package com.maninv.inventory_manager_api.application.dto;

import java.util.List;

// Modelo de visão para a resposta do caso de uso de consulta.
public record ProductInventoryView(
        String productId,
        String description,
        int totalStock,
        List<StoreStockDetailView> stockByStore
) {
    public record StoreStockDetailView(String storeId, int quantity) {}
}
