package com.maninv.inventory_manager_api.application.ports.in;

import com.maninv.inventory_manager_api.application.dto.ProductInventoryView;

public interface GetInventoryInfoUseCase {
    ProductInventoryView execute(String productId);
}
