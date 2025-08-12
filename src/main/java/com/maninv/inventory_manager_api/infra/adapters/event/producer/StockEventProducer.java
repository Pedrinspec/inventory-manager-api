package com.maninv.inventory_manager_api.infra.adapters.event.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(Object message) {
        try {
            log.info("Enviando evento para o tópico {}: {}", "${app.kafka.topic.stock-updates}", message);
            kafkaTemplate.send("${app.kafka.topic.stock-updates}", objectMapper.writeValueAsString(message));
        } catch (Exception e) {
            log.error("Error sending event to Kafka", e);
        }
    }
}
