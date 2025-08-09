package com.maninv.inventory_manager_api.application.dto;

// Comando para o caso de uso de atualização de estoque.
public record UpdateStockCommand(
        String storeId,
        String productId,
        int quantityChange
) {}
