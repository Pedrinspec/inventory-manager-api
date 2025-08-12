package com.maninv.inventory_manager_api.domain;

import com.maninv.inventory_manager_api.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryItemTest {

    @Test
    void create_shouldSucceed_whenInitialQuantityIsValid() {
        InventoryItem item = InventoryItem.create("SKU-001", "STORE-A", "Produto Teste", 10);

        assertNotNull(item);
        assertEquals("SKU-001", item.getProductId());
        assertEquals(10, item.getQuantity());
        assertNotNull(item.getLastUpdatedAt());
    }

    @Test
    void create_shouldThrowBusinessException_whenInitialQuantityIsNegative() {
        assertThrows(BusinessException.class, () -> InventoryItem.create("SKU-001", "STORE-A", "Produto Teste", -5));
    }

    @Test
    void decreaseStock_shouldSucceed_whenQuantityIsSufficient() {
        InventoryItem item = InventoryItem.create("SKU-001", "STORE-A", "Produto Teste", 20);

        item.decreaseStock(5);

        assertEquals(15, item.getQuantity());
    }

    @Test
    void decreaseStock_shouldThrowBusinessException_whenQuantityIsInsufficient() {
        InventoryItem item = InventoryItem.create("SKU-001", "STORE-A", "Produto Teste", 5);

        assertThrows(BusinessException.class, () -> item.decreaseStock(10));
    }

    @Test
    void decreaseStock_shouldThrowBusinessException_whenAmountIsZeroOrNegative() {
        InventoryItem item = InventoryItem.create("SKU-001", "STORE-A", "Produto Teste", 10);

        assertThrows(BusinessException.class, () -> item.decreaseStock(0));
        assertThrows(BusinessException.class, () -> item.decreaseStock(-5));
    }

    @Test
    void increaseStock_shouldSucceed_whenAmountIsValid() {
        InventoryItem item = InventoryItem.create("SKU-001", "STORE-A", "Produto Teste", 10);

        item.increaseStock(10);

        assertEquals(20, item.getQuantity());
    }

    @Test
    void increaseStock_shouldThrowBusinessException_whenAmountIsZeroOrNegative() {
        InventoryItem item = InventoryItem.create("SKU-001", "STORE-A", "Produto Teste", 10);

        assertThrows(BusinessException.class, () -> item.increaseStock(0));
        assertThrows(BusinessException.class, () -> item.increaseStock(-5));
    }
}