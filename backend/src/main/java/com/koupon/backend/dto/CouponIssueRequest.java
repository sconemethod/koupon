package com.koupon.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 

public class CouponIssueRequest {
    private String userId;
    private Long eventId;
}
