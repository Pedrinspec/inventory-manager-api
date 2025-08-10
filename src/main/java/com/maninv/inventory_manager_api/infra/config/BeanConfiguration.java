package com.maninv.inventory_manager_api.infra.config;

import com.maninv.inventory_manager_api.application.ports.in.CreateProductUseCase;
import com.maninv.inventory_manager_api.application.ports.in.GetInventoryInfoUseCase;
import com.maninv.inventory_manager_api.application.ports.in.UpdateStockUseCase;
import com.maninv.inventory_manager_api.application.ports.out.InventoryRepositoryPort;
import com.maninv.inventory_manager_api.application.usecases.CreateProductUseCaseImpl;
import com.maninv.inventory_manager_api.application.usecases.GetInventoryInfoUseCaseImpl;
import com.maninv.inventory_manager_api.application.usecases.UpdateStockUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CreateProductUseCase createProductUseCase(InventoryRepositoryPort inventoryRepositoryPort) {
        return new CreateProductUseCaseImpl(inventoryRepositoryPort);
    }

    @Bean
    public UpdateStockUseCase updateStockUseCase(InventoryRepositoryPort inventoryRepositoryPort) {
        return new UpdateStockUseCaseImpl(inventoryRepositoryPort);
    }

    @Bean
    public GetInventoryInfoUseCase getInventoryInfoUseCase(InventoryRepositoryPort inventoryRepositoryPort) {
        return new GetInventoryInfoUseCaseImpl(inventoryRepositoryPort);
    }
}
