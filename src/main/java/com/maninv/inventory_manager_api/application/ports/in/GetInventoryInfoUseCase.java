package com.maninv.inventory_manager_api.application.ports.in;

import com.maninv.inventory_manager_api.application.dto.ProductInventoryResponse;

public interface GetInventoryInfoUseCase {
    ProductInventoryResponse execute(String productId);
}
