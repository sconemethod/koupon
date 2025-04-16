// koupon/backend/src/main/java/com/koupon/backend/controller/CouponController.java

package main.java.com.koupon.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import main.java.com.koupon.backend.service.CouponService;
import main.java.com.koupon.backend.dto.CouponRequestDto;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/issue")
    public ResponseEntity<String> issueCoupon(@RequestBody CouponRequestDto dto) {
        couponService.issueCoupon(dto.getUserId(), dto.getCouponId());
        return ResponseEntity.ok("Coupon issued!");
    }
}
