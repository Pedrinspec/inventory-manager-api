package com.maninv.inventory_manager_api.infra.config;

import com.maninv.inventory_manager_api.InventoryManagerApplication;
import com.maninv.inventory_manager_api.application.ports.in.CreateProductUseCase;
import com.maninv.inventory_manager_api.application.ports.in.GetInventoryInfoUseCase;
import com.maninv.inventory_manager_api.application.ports.in.UpdateStockUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {
        InventoryManagerApplication.class
})
class BeanConfigurationTest {

    @Autowired
    private CreateProductUseCase createProductUseCase;

    @Autowired
    private GetInventoryInfoUseCase getInventoryInfoUseCase;

    @Autowired
    private UpdateStockUseCase updateStockUseCase;

    @Test
    void shouldLoadContextAndCreateUseCasesBeans() {
        assertNotNull(createProductUseCase);
        assertNotNull(getInventoryInfoUseCase);
        assertNotNull(updateStockUseCase);
    }
}
