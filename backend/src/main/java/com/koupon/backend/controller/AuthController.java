// koupon/backend/src/main/java/com/koupon/backend/controller/AuthController.java
package com.koupon.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import com.koupon.backend.service.AuthService;
import com.koupon.backend.dto.LoginRequest;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        authService.sendLoginEvent(request.getUserId());
        return ResponseEntity.ok("Login event sent!");
    }
}
