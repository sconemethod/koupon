package com.koupon.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String eventName;
    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer totalQuantity;
    private Integer remainingQuantity;

    private String creatorUserId;

    private LocalDateTime createdAt;
}
