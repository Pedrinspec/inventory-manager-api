package com.maninv.inventory_manager_api.application.ports.out;

import com.maninv.inventory_manager_api.domain.InventoryItem;

import java.util.List;
import java.util.Optional;

/**
 * Porta de Saída (Output Port): Define o contrato que a camada de aplicação
 * precisa que a camada de infraestrutura implemente. Neste caso, a persistência.
 */
public interface InventoryRepositoryPort {

    void save(InventoryItem inventoryItem);

    void saveAll(List<InventoryItem> items);

    Optional<InventoryItem> findByStoreIdAndProductId(String storeId, String productId);

    List<InventoryItem> findByProductId(String productId);
}
