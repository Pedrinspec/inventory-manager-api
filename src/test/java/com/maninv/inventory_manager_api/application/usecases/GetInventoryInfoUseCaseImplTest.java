package com.maninv.inventory_manager_api.application.usecases;

import com.maninv.inventory_manager_api.application.dto.ProductInventoryResponse;
import com.maninv.inventory_manager_api.application.ports.out.InventoryRepositoryPort;
import com.maninv.inventory_manager_api.domain.InventoryItem;
import com.maninv.inventory_manager_api.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetInventoryInfoUseCaseImplTest {

    @Mock
    private InventoryRepositoryPort inventoryRepositoryPort;

    @InjectMocks
    private GetInventoryInfoUseCaseImpl getInventoryInfoUseCase;

    @Test
    void shouldReturnCorrectInventoryInfoWhenItemsAreFound() {
        String productId = "SKU-XYZ";
        var item1 = InventoryItem.create(productId, "STORE-A", "Produto XYZ", 5);
        var item2 = InventoryItem.create(productId, "STORE-B", "Produto XYZ", 15);
        var items = List.of(item1, item2);

        when(inventoryRepositoryPort.findByProductId(productId)).thenReturn(items);

        ProductInventoryResponse response = getInventoryInfoUseCase.execute(productId);

        assertNotNull(response);
        assertEquals(productId, response.productId());
        assertEquals("Produto XYZ", response.description());
        assertEquals(20, response.totalStock());
        assertEquals(2, response.stockByStore().size());
        assertEquals("STORE-A", response.stockByStore().getFirst().storeId());
        assertEquals(5, response.stockByStore().getFirst().quantity());
    }

    @Test
    void shouldThrowBusinessExceptionWhenNoItemsAreFound() {
        String productId = "SKU-NOT-FOUND";

        when(inventoryRepositoryPort.findByProductId(productId)).thenReturn(Collections.emptyList());

        assertThrows(BusinessException.class, () -> getInventoryInfoUseCase.execute(productId));
    }
}