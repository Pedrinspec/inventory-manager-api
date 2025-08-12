package com.maninv.inventory_manager_api.application.ports.in;

import com.maninv.inventory_manager_api.application.dto.CreateProductCommand;

public interface CreateProductUseCase {
    void execute(CreateProductCommand command);
}

