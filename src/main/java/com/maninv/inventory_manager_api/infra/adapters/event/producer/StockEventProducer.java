package com.maninv.inventory_manager_api.infra.adapters.event.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    @Value("${app.kafka.topic.stock-updates}")
    private String topic = "${app.kafka.topic.stock-updates}";

    public void send(Object message) {
        try {
            log.info("Sending message to topic {}: {}", topic, message);
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(message));
        } catch (Exception e) {
            log.error("Error sending event to Kafka", e);
        }
    }
}
