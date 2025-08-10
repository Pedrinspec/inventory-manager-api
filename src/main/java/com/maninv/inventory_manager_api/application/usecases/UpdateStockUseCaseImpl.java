package com.maninv.inventory_manager_api.application.usecases;

import com.maninv.inventory_manager_api.application.dto.UpdateStockCommand;
import com.maninv.inventory_manager_api.application.ports.in.UpdateStockUseCase;
import com.maninv.inventory_manager_api.application.ports.out.InventoryRepositoryPort;
import com.maninv.inventory_manager_api.domain.InventoryItem;
import com.maninv.inventory_manager_api.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateStockUseCaseImpl implements UpdateStockUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    @Override
    public void execute(UpdateStockCommand command) {
        InventoryItem item = inventoryRepositoryPort
                .findByStoreIdAndProductId(command.storeId(), command.productId())
                .orElseThrow(() -> new BusinessException(
                        "Item de inventário não encontrado para o produto: " + command.productId() +
                                " na loja: " + command.storeId()));

        if (command.quantityChange() < 0) {
            item.decreaseStock(Math.abs(command.quantityChange()));
        } else {
            item.increaseStock(command.quantityChange());
        }

        inventoryRepositoryPort.update(item);
    }
}
