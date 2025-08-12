package com.maninv.inventory_manager_api.application.usecases;

import com.maninv.inventory_manager_api.application.dto.ProductInventoryResponse;
import com.maninv.inventory_manager_api.application.ports.in.GetInventoryInfoUseCase;
import com.maninv.inventory_manager_api.application.ports.out.InventoryRepositoryPort;
import com.maninv.inventory_manager_api.domain.InventoryItem;
import com.maninv.inventory_manager_api.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GetInventoryInfoUseCaseImpl implements GetInventoryInfoUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    @Override
    public ProductInventoryResponse execute(String productId) {
        List<InventoryItem> items = inventoryRepositoryPort.findByProductId(productId);

        if (items.isEmpty()) {
            throw new BusinessException("Nenhum item de inventário encontrado para o produto: " + productId);
        }

        String description = items.getFirst().getDescription();

        int totalStock = items.stream().mapToInt(InventoryItem::getQuantity).sum();

        List<ProductInventoryResponse.StoreStock> stockByStore = items.stream()
                .map(item -> new ProductInventoryResponse.StoreStock(item.getStoreId(), item.getQuantity()))
                .toList();

        return new ProductInventoryResponse(productId, description, totalStock, stockByStore);
    }
}
