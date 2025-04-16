package com.koupon.backend.dto;

import lombok.Data;

@Data
public class CouponRequestDto {
    private Long userId;
    private Long couponId;
}
