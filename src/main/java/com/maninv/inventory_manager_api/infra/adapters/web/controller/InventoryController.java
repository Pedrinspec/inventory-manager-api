package com.maninv.inventory_manager_api.infra.adapters.web.controller;

import com.maninv.inventory_manager_api.application.dto.ProductInventoryResponse;
import com.maninv.inventory_manager_api.application.ports.in.CreateProductUseCase;
import com.maninv.inventory_manager_api.application.ports.in.GetInventoryInfoUseCase;
import com.maninv.inventory_manager_api.infra.adapters.event.dto.StockEventDTO;
import com.maninv.inventory_manager_api.infra.adapters.event.producer.StockEventProducer;
import com.maninv.inventory_manager_api.infra.adapters.web.dto.CreateProductRequest;
import com.maninv.inventory_manager_api.infra.adapters.web.mapper.CreateProductMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Tag(name = "Inventory Management", description = "Endpoints para gerenciar e consultar o inventário")
public class InventoryController {

    private final CreateProductUseCase createProductUseCase;
    private final GetInventoryInfoUseCase getInventoryInfoUseCase;
    private final StockEventProducer stockEventProducer;
    private final CreateProductMapper createProductMapper;

    @PostMapping("/simulate/event")
    public ResponseEntity<String> simulateStockEvent(@RequestBody StockEventDTO event){
        log.info("Simulating stock event: {}", event);
        stockEventProducer.send(event);
        return ResponseEntity.ok("Event sent to Kafka topic successfully.");
    }

    @PostMapping("/products")
    public ResponseEntity<Void> createProduct(@Valid @RequestBody CreateProductRequest request) {
        log.info("Request to create product {}", request);
        createProductUseCase.execute(createProductMapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductInventoryResponse> getInventoryInfo(@PathVariable String productId) {
        log.info("Request find product inventory information: {}", productId);
        return ResponseEntity.ok(getInventoryInfoUseCase.execute(productId));
    }

}
