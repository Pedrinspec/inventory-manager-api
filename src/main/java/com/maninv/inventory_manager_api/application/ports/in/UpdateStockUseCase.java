package com.maninv.inventory_manager_api.application.ports.in;

import com.maninv.inventory_manager_api.application.dto.UpdateStockCommand;

public interface UpdateStockUseCase {
    void execute(UpdateStockCommand command);
}
