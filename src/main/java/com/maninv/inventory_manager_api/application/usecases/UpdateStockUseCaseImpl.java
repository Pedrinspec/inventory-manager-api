package com.maninv.inventory_manager_api.application.usecases;

import com.maninv.inventory_manager_api.application.dto.UpdateStockCommand;
import com.maninv.inventory_manager_api.application.ports.in.UpdateStockUseCase;
import com.maninv.inventory_manager_api.application.ports.out.InventoryRepositoryPort;
import com.maninv.inventory_manager_api.domain.InventoryItem;
import com.maninv.inventory_manager_api.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class UpdateStockUseCaseImpl implements UpdateStockUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    @Override
    public void execute(UpdateStockCommand command) {
        log.info("In execute UpdateStockUseCaseImpl");
        InventoryItem item = inventoryRepositoryPort
                .findByStoreIdAndProductId(command.storeId(), command.productId())
                .orElseThrow(() -> new BusinessException("Inventory item not found for product: " + command.productId() + " in store: " + command.storeId()));

        if (command.quantityChange() < 0) {
            log.error("Quantity to decrease processing: {}", command.quantityChange());
            item.decreaseStock(Math.abs(command.quantityChange()));
        } else {
            log.info("Quantity to increase processing: {}", command.quantityChange());
            item.increaseStock(command.quantityChange());
        }

        inventoryRepositoryPort.update(item);
    }
}
