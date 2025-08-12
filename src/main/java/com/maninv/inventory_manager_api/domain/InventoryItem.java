package com.maninv.inventory_manager_api.domain;

import com.maninv.inventory_manager_api.domain.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class InventoryItem {

    private final String productId;
    private final String storeId;
    private String description;
    private Integer quantity;
    private LocalDateTime lastUpdatedAt;

    public static InventoryItem create(String productId, String storeId, String description, Integer initialQuantity) {
        if (initialQuantity < 0) {
            throw new BusinessException("A quantidade inicial do estoque não pode ser negativa.");
        }
        return new InventoryItem(productId, storeId, description, initialQuantity, LocalDateTime.now());
    }

    public void decreaseStock(Integer quantityToDecrease) {
        if (quantityToDecrease <= 0) {
            throw new BusinessException("A quantidade a ser diminuída deve ser positiva.");
        }
        if (this.quantity < quantityToDecrease) {
            throw new BusinessException("Estoque insuficiente para o produto: " + this.productId);
        }
        this.quantity -= quantityToDecrease;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void increaseStock(Integer quantityToDecrease) {
        if (quantityToDecrease <= 0) {
            throw new BusinessException("A quantidade a ser aumentada deve ser positiva.");
        }
        this.quantity += quantityToDecrease;
        this.lastUpdatedAt = LocalDateTime.now();
    }

}
