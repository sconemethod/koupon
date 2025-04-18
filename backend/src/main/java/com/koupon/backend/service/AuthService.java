package com.koupon.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class AuthService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendLoginEvent(String userId) {
        List<Map<String, Object>> fields = new ArrayList<>();
        Map<String, Object> field1 = new HashMap<>();
        field1.put("field", "user_id");
        field1.put("type", "string");

        Map<String, Object> field2 = new HashMap<>();
        field2.put("field", "login_at");
        field2.put("type", "string");

        fields.add(field1);
        fields.add(field2);

        Map<String, Object> schema = new HashMap<>();
        schema.put("type", "struct");
        schema.put("fields", fields);
        schema.put("optional", false);
        schema.put("name", "UserLogin");

        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("login_at", LocalDateTime.now().toString());

        Map<String, Object> message = new HashMap<>();
        message.put("schema", schema);
        message.put("payload", payload);

        kafkaTemplate.send("user-login", message);
    }
}
