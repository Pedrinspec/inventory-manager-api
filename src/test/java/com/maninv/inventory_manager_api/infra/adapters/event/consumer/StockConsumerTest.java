package com.maninv.inventory_manager_api.infra.adapters.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maninv.inventory_manager_api.application.dto.UpdateStockCommand;
import com.maninv.inventory_manager_api.application.ports.in.UpdateStockUseCase;
import com.maninv.inventory_manager_api.infra.adapters.event.dto.StockEventDTO;
import com.maninv.inventory_manager_api.infra.adapters.event.mapper.StockMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockConsumerTest {

    @Mock
    private UpdateStockUseCase updateStockUseCase;

    @Mock
    private StockMapper stockMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private StockConsumer stockConsumer;

    @Test
    void handleStockChangedEvent_withValidMessage_shouldProcessSuccessfully() throws JsonProcessingException {
        String validJson = "{\"eventId\":\"uuid-123\",\"payload\":{\"productId\":\"SKU-001\",\"storeId\":\"STORE-A\",\"quantityChange\":-1}}";
        var stockEventDTO = new StockEventDTO("uuid-123", null, null, new StockEventDTO.StockDTO("STORE-A", "SKU-001", -1));
        var updateStockCommand = new UpdateStockCommand("STORE-A", "SKU-001", -1);

        when(objectMapper.readValue(validJson, StockEventDTO.class)).thenReturn(stockEventDTO);
        when(stockMapper.toCommand(stockEventDTO)).thenReturn(updateStockCommand);

        stockConsumer.handleStockChangedEvent(validJson, "stock-updates-topic");

        verify(objectMapper).readValue(validJson, StockEventDTO.class);
        verify(stockMapper).toCommand(stockEventDTO);
        verify(updateStockUseCase).execute(updateStockCommand);
    }

    @Test
    void handleStockChangedEvent_withInvalidJson_shouldThrowRuntimeException() throws JsonProcessingException {
        String invalidJson = "{\"eventId\":\"uuid-123\", invalid}";
        when(objectMapper.readValue(invalidJson, StockEventDTO.class)).thenThrow(JsonProcessingException.class);

        assertThrows(RuntimeException.class, () -> stockConsumer.handleStockChangedEvent(invalidJson, "stock-updates-topic"));

        verify(updateStockUseCase, never()).execute(any());
    }

    @Test
    void handleStockChangedEvent_withInvalidEventContent_shouldThrowRuntimeException() throws JsonProcessingException {
        String jsonWithInvalidContent = "{\"eventId\":null,\"payload\":{\"productId\":\"SKU-001\",\"storeId\":\"STORE-A\",\"quantityChange\":-1}}";
        var stockEventDTO = new StockEventDTO(null, null, null, new StockEventDTO.StockDTO("STORE-A", "SKU-001", -1));

        when(objectMapper.readValue(jsonWithInvalidContent, StockEventDTO.class)).thenReturn(stockEventDTO);

        assertThrows(RuntimeException.class, () -> stockConsumer.handleStockChangedEvent(jsonWithInvalidContent, "stock-updates-topic"));

        verify(updateStockUseCase, never()).execute(any());
    }
}
