package com.maninv.inventory_manager_api.application.ports.out;

import com.maninv.inventory_manager_api.domain.InventoryItem;

import java.util.List;
import java.util.Optional;

public interface InventoryRepositoryPort {

    void update(InventoryItem inventoryItem);

    void saveAll(List<InventoryItem> items);

    Optional<InventoryItem> findByStoreIdAndProductId(String storeId, String productId);

    List<InventoryItem> findByProductId(String productId);
}
