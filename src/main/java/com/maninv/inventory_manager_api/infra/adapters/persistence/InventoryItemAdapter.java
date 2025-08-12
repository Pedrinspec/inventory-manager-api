package com.maninv.inventory_manager_api.infra.adapters.persistence;

import com.maninv.inventory_manager_api.application.ports.out.InventoryRepositoryPort;
import com.maninv.inventory_manager_api.domain.InventoryItem;
import com.maninv.inventory_manager_api.infra.adapters.persistence.entity.InventoryItemEntity;
import com.maninv.inventory_manager_api.infra.adapters.persistence.mapper.InventoryItemMapper;
import com.maninv.inventory_manager_api.infra.adapters.persistence.repository.InventoryItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryItemAdapter implements InventoryRepositoryPort {

    private final InventoryItemRepository repository;
    private final InventoryItemMapper mapper;

    @Override
    @Cacheable(value = "inventory", key = "#productId")
    public List<InventoryItem> findByProductId(String productId) {
        log.info("Finding inventory items by productId: {}", productId);
        return mapper.toDomainList(repository.findByProductId(productId));
    }

    @Override
    @CacheEvict(value = "inventory", key = "#inventoryItem.productId")
    @Transactional
    public void update(InventoryItem inventoryItem) {
        log.info("Updating inventory item: {}", inventoryItem);
        repository.findByStoreIdAndProductId(inventoryItem.getStoreId(), inventoryItem.getProductId())
                .ifPresent(entity -> {
                    InventoryItemEntity inventoryItemEntity = mapper.toEntity(inventoryItem);
                    inventoryItemEntity.setId(entity.getId());
                });
    }

    @Override
    public void saveAll(List<InventoryItem> items) {
        log.info("Saving inventory items: {}", items);
        repository.saveAll(mapper.toEntityList(items));
    }

    @Override
    public Optional<InventoryItem> findByStoreIdAndProductId(String storeId, String productId) {
        log.info("Finding inventory item by storeId: {} and productId: {}", storeId, productId);
        return repository.findByStoreIdAndProductId(storeId, productId)
                .map(mapper::toDomain);
    }
}
