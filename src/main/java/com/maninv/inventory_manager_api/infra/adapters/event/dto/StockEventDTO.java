package com.maninv.inventory_manager_api.infra.adapters.event.dto;

public record StockEventDTO(
        String eventId,
        String timestamp,
        String eventType,
        StockDTO stock
) {
    public record StockDTO(
            String storeId,
            String productId,
            Integer quantityChange
    ) {}

    public void validate() {
        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("O eventId não pode ser nulo ou vazio.");
        }
        if (stock == null) {
            throw new IllegalArgumentException("O stock do evento não pode ser nulo.");
        }
        if (stock.productId() == null || stock.productId().isBlank()) {
            throw new IllegalArgumentException("O productId no stock não pode ser nulo ou vazio.");
        }
        if (stock.storeId() == null || stock.storeId().isBlank()) {
            throw new IllegalArgumentException("O storeId no stock não pode ser nulo ou vazio.");
        }
    }
}
