package com.koupon.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCouponIssueEvent(String userId, String couponCode) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("coupon_code", couponCode);
        payload.put("issued_at", LocalDateTime.now().toString());

        Map<String, Object> message = new HashMap<>();
        message.put("schema", null); // Connect에서는 JSON schema 없이 schemas.enable=true로 파싱
        message.put("payload", payload);

        kafkaTemplate.send("coupon-issue", message);
    }
}
