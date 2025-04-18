// koupon/backend/src/main/java/com/koupon/backend/controller/CouponController.java

package com.koupon.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import com.koupon.backend.service.CouponService;
import com.koupon.backend.dto.CouponIssueRequest;
import com.koupon.backend.domain.Coupon;
import org.springframework.data.redis.core.RedisTemplate;
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;
    private final RedisTemplate<String, String> redisTemplate;
    @PostMapping("/issue")
    public ResponseEntity<?> issueCoupon(@RequestBody CouponIssueRequest request) {
        try {
            Coupon issued = couponService.issueCoupon(request.getUserId(), request.getEventId());
            return ResponseEntity.ok("쿠폰 발급 성공! 🎟️ CODE: " + issued.getCouponCode());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("발급 실패 😢 " + e.getMessage());
        }
    }
}
