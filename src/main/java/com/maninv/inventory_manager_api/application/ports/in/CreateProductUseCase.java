package com.maninv.inventory_manager_api.application.ports.in;

import com.maninv.inventory_manager_api.application.dto.CreateProductCommand;

/**
 * Portas de Entrada (Input Ports): Definem os contratos dos casos de uso.
 * São as interfaces que a camada de infraestrutura (controllers, consumers) usará
 * para invocar a lógica de negócio.
 */
public interface CreateProductUseCase {
    void execute(CreateProductCommand command);
}

