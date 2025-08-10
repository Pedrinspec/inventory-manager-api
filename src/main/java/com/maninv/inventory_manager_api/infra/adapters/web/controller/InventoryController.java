package com.maninv.inventory_manager_api.infra.adapters.web.controller;

import com.maninv.inventory_manager_api.application.dto.ProductInventoryView;
import com.maninv.inventory_manager_api.application.ports.in.CreateProductUseCase;
import com.maninv.inventory_manager_api.application.ports.in.GetInventoryInfoUseCase;
import com.maninv.inventory_manager_api.infra.adapters.web.dto.CreateProductRequest;
import com.maninv.inventory_manager_api.infra.adapters.web.mapper.CreateProductMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final CreateProductUseCase createProductUseCase;
    private final GetInventoryInfoUseCase getInventoryInfoUseCase;
    private final CreateProductMapper createProductMapper;

    @PostMapping("/products")
    public ResponseEntity<Void> createProduct(@Valid @RequestBody CreateProductRequest request) {
        createProductUseCase.execute(createProductMapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductInventoryView> getInventoryInfo(@PathVariable String productId) {
        return ResponseEntity.ok(getInventoryInfoUseCase.execute(productId));
    }

}
