package com.maninv.inventory_manager_api.infra.adapters.persistence.repository;

import com.maninv.inventory_manager_api.domain.InventoryItem;
import com.maninv.inventory_manager_api.infra.adapters.persistence.entity.InventoryItemEntity;
import com.maninv.inventory_manager_api.infra.adapters.persistence.mapper.InventoryItemMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryItemAdapterTest {

    @Mock
    private InventoryItemRepository repository;

    @Mock
    private InventoryItemMapper mapper;

    @InjectMocks
    private InventoryItemAdapter inventoryItemAdapter;

    @Test
    void shouldFindItemsByProductId() {
        var entityList = List.of(new InventoryItemEntity());
        var domainList = List.of(new InventoryItem("SKU-123", "STORE-A", "Test", 10, null));
        when(repository.findByProductId("SKU-123")).thenReturn(entityList);
        when(mapper.toDomainList(entityList)).thenReturn(domainList);

        List<InventoryItem> result = inventoryItemAdapter.findByProductId("SKU-123");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SKU-123", result.getFirst().getProductId());
        verify(repository).findByProductId("SKU-123");
        verify(mapper).toDomainList(entityList);
    }

    @Test
    void shouldUpdateExistingInventoryItem() {
        var domainItemToUpdate = new InventoryItem("SKU-456", "STORE-C", "Produto Atualizado", 45, LocalDateTime.now());
        var existingEntity = new InventoryItemEntity();
        existingEntity.setId(1L);
        existingEntity.setProductId("SKU-456");
        existingEntity.setStoreId("STORE-C");
        existingEntity.setDescription("Produto Existente");
        existingEntity.setQuantity(30);
        existingEntity.setLastUpdatedAt(LocalDateTime.now().minusDays(1));

        when(repository.findByStoreIdAndProductId(any(), any())).thenReturn(Optional.of(existingEntity));
        when(mapper.toEntity(any())).thenReturn(existingEntity);

        Assertions.assertDoesNotThrow(() -> inventoryItemAdapter.update(domainItemToUpdate));
    }

    @Test
    void shouldSaveAllInventoryItems() {
        var domainList = List.of(InventoryItem.create("SKU-789", "STORE-A", "Produto Novo 1", 5));
        var entityList = List.of(new InventoryItemEntity());
        when(mapper.toEntityList(domainList)).thenReturn(entityList);

        inventoryItemAdapter.saveAll(domainList);

        verify(mapper).toEntityList(domainList);
        verify(repository).saveAll(entityList);
    }

    @Test
    void shouldFindByStoreIdAndProductIdWhenExists() {
        var entity = new InventoryItemEntity();
        var domainItem = new InventoryItem("SKU-ABC", "STORE-D", "Test", 100, null);
        when(repository.findByStoreIdAndProductId("STORE-D", "SKU-ABC")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domainItem);

        Optional<InventoryItem> result = inventoryItemAdapter.findByStoreIdAndProductId("STORE-D", "SKU-ABC");

        assertTrue(result.isPresent());
        assertEquals("SKU-ABC", result.get().getProductId());
        verify(repository).findByStoreIdAndProductId("STORE-D", "SKU-ABC");
        verify(mapper).toDomain(entity);
    }

    @Test
    void shouldReturnEmptyOptionalWhenItemDoesNotExist() {
        when(repository.findByStoreIdAndProductId("STORE-Z", "SKU-XYZ")).thenReturn(Optional.empty());

        Optional<InventoryItem> result = inventoryItemAdapter.findByStoreIdAndProductId("STORE-Z", "SKU-XYZ");

        assertTrue(result.isEmpty());
        verify(repository).findByStoreIdAndProductId("STORE-Z", "SKU-XYZ");
        verify(mapper, never()).toDomain(any());
    }
}