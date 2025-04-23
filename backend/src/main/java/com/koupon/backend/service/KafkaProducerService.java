package com.koupon.backend.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
  private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);
  private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCouponIssueEvent(String userId, String couponCode) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("coupon_code", couponCode);
        payload.put("issued_at", LocalDateTime.now().toString());

        Map<String, Object> message = new HashMap<>();
        message.put("schema", null); // Connect에서는 JSON schema 없이 schemas.enable=true로 파싱
        message.put("payload", payload);

        ListenableFuture<SendResult<String, Object>> future = 
        kafkaTemplate.send("coupon-issue", message);

        future.addCallback(new ListenableFutureCallback<>() {
          @Override
          public void onSuccess(SendResult<String, Object> result) {
              log.info("Kafka send success: partition={}, offset={}",
                       result.getRecordMetadata().partition(),
                       result.getRecordMetadata().offset());
          }
          @Override
          public void onFailure(Throwable ex) {
              log.error("Kafka send failed for {}: {}", payload, ex.getMessage());
          }
      });
    }
}
