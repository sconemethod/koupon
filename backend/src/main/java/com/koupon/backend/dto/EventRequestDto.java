package com.koupon.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EventRequestDto {
    private String eventName;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalQuantity;
    private String creatorUserId;
}
