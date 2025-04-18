package com.koupon.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CouponIssueResponse {
    private String couponCode;
    private boolean success;
    private String message;
}
