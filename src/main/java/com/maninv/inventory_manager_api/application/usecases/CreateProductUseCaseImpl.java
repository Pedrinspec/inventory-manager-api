package com.maninv.inventory_manager_api.application.usecases;

import com.maninv.inventory_manager_api.application.dto.CreateProductCommand;
import com.maninv.inventory_manager_api.application.ports.in.CreateProductUseCase;
import com.maninv.inventory_manager_api.application.ports.out.InventoryRepositoryPort;
import com.maninv.inventory_manager_api.domain.InventoryItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CreateProductUseCaseImpl implements CreateProductUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    @Override
    public void execute(CreateProductCommand command) {
        log.info("Executando CreateProductUseCaseImpl");
        List<InventoryItem> items = command.initialStock().stream()
                .map(stock -> InventoryItem.create(
                        command.productId(),
                        stock.storeId(),
                        command.description(),
                        stock.quantity()))
                .toList();

        inventoryRepositoryPort.saveAll(items);
    }
}
