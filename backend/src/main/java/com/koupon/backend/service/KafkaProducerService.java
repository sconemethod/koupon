package com.koupon.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Counter couponSentCounter;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate,
                                MeterRegistry meterRegistry) {
        this.kafkaTemplate = kafkaTemplate;
        this.couponSentCounter = meterRegistry.counter("coupon.issued.sent");
    }

    public void sendCouponIssueEvent(String userId, String couponCode) {
        // 전송 카운터 증가
        couponSentCounter.increment();

        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("coupon_code", couponCode);
        payload.put("issued_at", LocalDateTime.now().toString());

        Map<String, Object> message = new HashMap<>();
        message.put("schema", null);
        message.put("payload", payload);

        // CompletableFuture 로 변경
        CompletableFuture<SendResult<String, Object>> future =
            kafkaTemplate.send("coupon-issue", message);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Kafka send failed for {}: {}", payload, ex.getMessage());
            } else {
                log.info("Kafka send success: partition={}, offset={}",
                         result.getRecordMetadata().partition(),
                         result.getRecordMetadata().offset());
            }
        });
    }
}
