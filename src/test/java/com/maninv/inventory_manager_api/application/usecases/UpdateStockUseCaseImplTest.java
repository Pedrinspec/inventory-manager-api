package com.maninv.inventory_manager_api.application.usecases;

import com.maninv.inventory_manager_api.application.dto.UpdateStockCommand;
import com.maninv.inventory_manager_api.application.ports.out.InventoryRepositoryPort;
import com.maninv.inventory_manager_api.domain.InventoryItem;
import com.maninv.inventory_manager_api.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateStockUseCaseImplTest {

    @Mock
    private InventoryRepositoryPort inventoryRepositoryPort;

    @InjectMocks
    private UpdateStockUseCaseImpl updateStockUseCase;

    @Test
    void shouldDecreaseStockWhenQuantityChangeIsNegative() {
        String productId = "SKU-123";
        String storeId = "STORE-A";
        var command = new UpdateStockCommand(storeId, productId, -5);
        var item = mock(InventoryItem.class);

        when(inventoryRepositoryPort.findByStoreIdAndProductId(storeId, productId)).thenReturn(Optional.of(item));

        updateStockUseCase.execute(command);

        verify(item).decreaseStock(5);
        verify(inventoryRepositoryPort).update(item);
    }

    @Test
    void shouldIncreaseStockWhenQuantityChangeIsPositive() {
        String productId = "SKU-123";
        String storeId = "STORE-A";
        var command = new UpdateStockCommand(storeId, productId, 10);
        var item = mock(InventoryItem.class);

        when(inventoryRepositoryPort.findByStoreIdAndProductId(storeId, productId)).thenReturn(Optional.of(item));

        updateStockUseCase.execute(command);

        verify(item).increaseStock(10);
        verify(inventoryRepositoryPort).update(item);
    }

    @Test
    void shouldThrowBusinessExceptionWhenItemIsNotFound() {
        String productId = "SKU-NOT-FOUND";
        String storeId = "STORE-X";
        var command = new UpdateStockCommand(storeId, productId, -1);

        when(inventoryRepositoryPort.findByStoreIdAndProductId(storeId, productId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> updateStockUseCase.execute(command));

        verify(inventoryRepositoryPort, never()).update(any());
    }
}