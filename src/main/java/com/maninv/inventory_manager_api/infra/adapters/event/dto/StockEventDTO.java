package com.maninv.inventory_manager_api.infra.adapters.event.dto;

public record StockEventDTO(
        String eventId,
        String timestamp,
        String eventType,
        PayloadDTO payload
) {
    public record PayloadDTO(
            String storeId,
            String productId,
            int quantityChange
    ) {}
}
