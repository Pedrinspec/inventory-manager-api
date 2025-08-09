package com.maninv.inventory_manager_api.domain;

import com.maninv.inventory_manager_api.domain.exception.BusinessException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class InventoryItem {

    private final String productId;
    private final String storeId;

    @Setter
    private String description;

    private int quantity;
    private LocalDateTime lastUpdatedAt;

    private InventoryItem(String productId, String storeId, String description, int quantity) {
        this.productId = productId;
        this.storeId = storeId;
        this.description = description;
        this.quantity = quantity;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    // permite com que o construtor seja privado e a criação do objeto seja feita por meio estático e nunca seja um estado inválido
    public static InventoryItem create(String productId, String storeId, String description, int initialQuantity) {
        if (initialQuantity < 0) {
            throw new BusinessException("A quantidade inicial do estoque não pode ser negativa.");
        }
        return new InventoryItem(productId, storeId, description, initialQuantity);
    }

    public void decreaseStock(int amountToDecrease) {
        if (amountToDecrease <= 0) {
            throw new BusinessException("A quantidade a ser diminuída deve ser positiva.");
        }
        if (this.quantity < amountToDecrease) {
            throw new BusinessException("Estoque insuficiente para o produto: " + this.productId);
        }
        this.quantity -= amountToDecrease;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void increaseStock(int amountToIncrease) {
        if (amountToIncrease <= 0) {
            throw new BusinessException("A quantidade a ser aumentada deve ser positiva.");
        }
        this.quantity += amountToIncrease;
        this.lastUpdatedAt = LocalDateTime.now();
    }

}
