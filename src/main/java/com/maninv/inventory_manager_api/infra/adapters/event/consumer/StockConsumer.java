package com.maninv.inventory_manager_api.infra.adapters.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maninv.inventory_manager_api.application.ports.in.UpdateStockUseCase;
import com.maninv.inventory_manager_api.infra.adapters.event.dto.StockEventDTO;
import com.maninv.inventory_manager_api.infra.adapters.event.mapper.StockMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockConsumer {

    private final UpdateStockUseCase updateStockUseCase;
    private final StockMapper stockMapper;
    private final ObjectMapper objectMapper;

    @RetryableTopic(
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    @KafkaListener(topics = "${app.kafka.topic.stock-updates}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleStockChangedEvent(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Message received from topic {}: {}", topic, message);
        try {
            var stockEvent = objectMapper.readValue(message, StockEventDTO.class);
            stockEvent.validate();
            updateStockUseCase.execute(stockMapper.toCommand(stockEvent));
            log.info("Event {} successfully processed.", stockEvent.eventId());
        } catch (Exception ex) {
            log.error("Business or deserialization error while processing event. Throwing retry/DLQ.", ex);
            throw new RuntimeException(ex);
        }
    }
}
