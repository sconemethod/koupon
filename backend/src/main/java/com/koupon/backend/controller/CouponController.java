// koupon/backend/src/main/java/com/koupon/backend/controller/CouponController.java

package com.koupon.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import com.koupon.backend.service.CouponService;
import com.koupon.backend.dto.CouponRequestDto;


@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor

public class CouponController {

    private final CouponService couponService;

    // 쿠폰 발급
    @PostMapping("/issue")
    public ResponseEntity<String> issueCoupon(@RequestBody CouponRequestDto dto) {
        couponService.issueCoupon(dto.getUserId());
        return ResponseEntity.ok("Coupon issued!");
    }
}
