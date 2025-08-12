package com.maninv.inventory_manager_api.application.usecases;

import com.maninv.inventory_manager_api.application.dto.ProductInventoryResponse;
import com.maninv.inventory_manager_api.application.ports.in.GetInventoryInfoUseCase;
import com.maninv.inventory_manager_api.application.ports.out.InventoryRepositoryPort;
import com.maninv.inventory_manager_api.domain.InventoryItem;
import com.maninv.inventory_manager_api.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GetInventoryInfoUseCaseImpl implements GetInventoryInfoUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    @Override
    public ProductInventoryResponse execute(String productId) {
        log.info("In execute method GetInventoryInfoUseCase...");
        List<InventoryItem> items = inventoryRepositoryPort.findByProductId(productId);

        if (items.isEmpty()) {
            throw new BusinessException("Inventory items not found for the product: " + productId);
        }

        String description = items.getFirst().getDescription();

        Integer totalStock = items.stream().mapToInt(InventoryItem::getQuantity).sum();

        List<ProductInventoryResponse.StoreStock> stockByStore = items.stream()
                .map(item -> new ProductInventoryResponse.StoreStock(item.getStoreId(), item.getQuantity()))
                .toList();

        return new ProductInventoryResponse(productId, description, totalStock, stockByStore);
    }
}
