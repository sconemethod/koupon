// koupon/backend/src/main/java/com/koupon/backend/domain/Coupon.java

package com.koupon.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Integer totalCount;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private LocalDateTime createdAt;

    // (2) CouponIssue 테이블과 1:N 관계
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private List<CouponIssue> couponIssues;
}
