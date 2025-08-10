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
            log.info("Enviando evento para o tópico {}: {}", "stock-updates-topic", message);
            kafkaTemplate.send("stock-updates-topic", objectMapper.writeValueAsString(message));
        } catch (Exception e) {
            log.error("Erro ao enviar evento para o Kafka", e);
        }
    }
}
