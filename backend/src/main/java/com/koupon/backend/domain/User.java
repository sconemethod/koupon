// koupon/backend/src/main/java/com/koupon/backend/domain/User.java

package com.koupon.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    private LocalDateTime createdAt;

    // (1) CouponIssue 테이블과 1:N 관계 설정
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CouponIssue> couponIssues;
}
