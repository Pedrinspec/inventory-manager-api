package com.maninv.inventory_manager_api.application.usecases;

import com.maninv.inventory_manager_api.application.dto.CreateProductCommand;
import com.maninv.inventory_manager_api.application.ports.out.InventoryRepositoryPort;
import com.maninv.inventory_manager_api.domain.InventoryItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseImplTest {

    @Mock
    private InventoryRepositoryPort inventoryRepositoryPort;

    @InjectMocks
    private CreateProductUseCaseImpl createProductUseCase;

    @Captor
    private ArgumentCaptor<List<InventoryItem>> inventoryItemListCaptor;

    @Test
    void shouldCorrectlyMapCommandToDomainObjectsAndSave() {
        var initialStock = List.of(
                new CreateProductCommand.InitialStock("STORE-A", 10),
                new CreateProductCommand.InitialStock("STORE-B", 20)
        );
        var command = new CreateProductCommand("SKU-001", "Produto Teste", initialStock);

        createProductUseCase.execute(command);

        verify(inventoryRepositoryPort).saveAll(inventoryItemListCaptor.capture());

        List<InventoryItem> capturedItems = inventoryItemListCaptor.getValue();

        assertNotNull(capturedItems);
        assertEquals(2, capturedItems.size());
        assertEquals("SKU-001", capturedItems.getFirst().getProductId());
        assertEquals("STORE-A", capturedItems.get(0).getStoreId());
        assertEquals(10, capturedItems.get(0).getQuantity());
        assertEquals("STORE-B", capturedItems.get(1).getStoreId());
        assertEquals(20, capturedItems.get(1).getQuantity());
    }

    @Test
    void shouldHandleCommandWithEmptyInitialStock() {
        var command = new CreateProductCommand("SKU-002", "Produto Sem Estoque", List.of());

        createProductUseCase.execute(command);

        verify(inventoryRepositoryPort).saveAll(inventoryItemListCaptor.capture());

        List<InventoryItem> capturedItems = inventoryItemListCaptor.getValue();
        assertNotNull(capturedItems);
        assertEquals(0, capturedItems.size());
    }
}