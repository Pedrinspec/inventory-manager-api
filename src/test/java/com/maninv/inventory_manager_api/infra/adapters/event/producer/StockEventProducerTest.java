package com.maninv.inventory_manager_api.infra.adapters.event.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockEventProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private StockEventProducer stockEventProducer;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(stockEventProducer, "topic", "stock-updates-topic");
    }

    @Test
    void sendShouldSerializeMessageAndSendToKafka() throws JsonProcessingException {
        var messageObject = new Object();
        String expectedJson = "{\"key\":\"value\"}";
        when(objectMapper.writeValueAsString(messageObject)).thenReturn(expectedJson);

        stockEventProducer.send(messageObject);

        verify(objectMapper).writeValueAsString(messageObject);
        verify(kafkaTemplate).send("stock-updates-topic", expectedJson);
    }

    @Test
    void sendShouldLogAndNotSendWhenSerializationFails() throws JsonProcessingException {
        var messageObject = new Object();
        when(objectMapper.writeValueAsString(messageObject)).thenThrow(new JsonProcessingException("Test Exception") {});

        stockEventProducer.send(messageObject);

        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }
}