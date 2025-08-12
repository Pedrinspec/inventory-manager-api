package com.maninv.inventory_manager_api.infra.adapters.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maninv.inventory_manager_api.application.dto.CreateProductCommand;
import com.maninv.inventory_manager_api.application.dto.ProductInventoryResponse;
import com.maninv.inventory_manager_api.application.ports.in.CreateProductUseCase;
import com.maninv.inventory_manager_api.application.ports.in.GetInventoryInfoUseCase;
import com.maninv.inventory_manager_api.infra.adapters.event.dto.StockEventDTO;
import com.maninv.inventory_manager_api.infra.adapters.event.producer.StockEventProducer;
import com.maninv.inventory_manager_api.infra.adapters.web.dto.CreateProductRequest;
import com.maninv.inventory_manager_api.infra.adapters.web.mapper.CreateProductMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateProductUseCase createProductUseCase;

    @MockBean
    private GetInventoryInfoUseCase getInventoryInfoUseCase;

    @MockBean
    private StockEventProducer stockEventProducer;

    @MockBean
    private CreateProductMapper createProductMapper;

    @Test
    void createProduct_ShouldReturnCreated() throws Exception {
        var request = new CreateProductRequest("SKU-001", "Produto Teste", List.of(new CreateProductRequest.InitialStockItem("1", 100)));
        var command = new CreateProductCommand("SKU-001", "Produto Teste", List.of(new CreateProductCommand.InitialStock("1", 100)));

        when(createProductMapper.toCommand(any())).thenReturn(command);

        ResultActions result = mockMvc.perform(post("/api/v1/inventory/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isCreated());
        verify(createProductUseCase).execute(command);
    }

    @Test
    void getInventoryInfoShouldReturnInventoryData() throws Exception {
        String productId = "SKU-001";
        var responseDto = new ProductInventoryResponse(productId, "Produto Teste", 100, Collections.emptyList());

        when(getInventoryInfoUseCase.execute(productId)).thenReturn(responseDto);

        ResultActions result = mockMvc.perform(get("/api/v1/inventory/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.description").value("Produto Teste"))
                .andExpect(jsonPath("$.totalStock").value(100));
    }

    @Test
    void simulateStockEventShouldReturnOk() throws Exception {
        var event = new StockEventDTO("uuid-123", "2025-01-01T00:00:00Z", "STOCK_DECREASED", null);

        ResultActions result = mockMvc.perform(post("/api/v1/inventory/simulate/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)));

        result.andExpect(status().isOk());
        verify(stockEventProducer).send(any(StockEventDTO.class));
    }
}