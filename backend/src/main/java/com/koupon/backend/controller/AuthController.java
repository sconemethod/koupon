// koupon/backend/src/main/java/com/koupon/backend/controller/AuthController.java
package com.koupon.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import com.koupon.backend.service.AuthService;
import com.koupon.backend.dto.LoginRequest;
import com.koupon.backend.domain.User;
import com.koupon.backend.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        // ✅ 유저가 없으면 저장 (authService에서 처리)
        authService.ensureUserExists(request.getUserId());

        // ✅ Kafka 메시지 발송
        authService.sendLoginEvent(request.getUserId());

        return ResponseEntity.ok("Login success and event sent!");
    }
}
