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
    public void handleStockChangedEvent(String eventJson, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Recebido evento do tópico {}: {}", topic, eventJson);
        try {
            var eventDto = objectMapper.readValue(eventJson, StockEventDTO.class);
            updateStockUseCase.execute(stockMapper.toCommand(eventDto));
            log.info("Evento {} processado com sucesso.", eventDto.eventId());
        } catch (Exception ex) {
            log.error("Erro de negócio ou desserialização ao processar evento. Lançando para retentativa/DLQ.", ex);
            throw new RuntimeException(ex);
        }
    }
}
