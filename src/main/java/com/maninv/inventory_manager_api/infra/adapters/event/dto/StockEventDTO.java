package com.maninv.inventory_manager_api.infra.adapters.event.dto;

import com.maninv.inventory_manager_api.domain.exception.BusinessException;

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
            throw new BusinessException("EventId cannot be null.");
        }
        if (stock == null) {
            throw new BusinessException("stock cannot be null.");
        }
        if (stock.productId() == null || stock.productId().isBlank()) {
            throw new BusinessException("productId cannot be null.");
        }
        if (stock.storeId() == null || stock.storeId().isBlank()) {
            throw new BusinessException("storeId cannot be null.");
        }
        if (stock.quantityChange() == null || stock.quantityChange() == 0) {
            throw new BusinessException("QuantityChange cannot be null or zero.");
        }
    }
}
