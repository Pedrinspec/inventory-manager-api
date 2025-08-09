package com.maninv.inventory_manager_api.application.dto;

import java.util.List;

/**
 * DTOs (ou mais precisamente, Command e Query objects) para a camada de aplicação.
 * Estes objetos transportam dados para dentro e para fora dos casos de uso.
 */

// Comando para o caso de uso de criação de produto.
public record CreateProductCommand(
        String productId,
        String description,
        List<InitialStock> initialStock
) {
    public record InitialStock(String storeId, int quantity) {}
}
