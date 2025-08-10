package com.maninv.inventory_manager_api.infra.adapters.persistence.repository;

import com.maninv.inventory_manager_api.infra.adapters.persistence.entity.InventoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, Long> {

    Optional<InventoryItemEntity> findByStoreIdAndProductId(String storeId, String productId);

    List<InventoryItemEntity> findByProductId(String productId);

}
