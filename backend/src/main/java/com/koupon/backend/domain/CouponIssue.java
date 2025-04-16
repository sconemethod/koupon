// koupon/backend/src/main/java/com/koupon/backend/domain/CouponIssue.java

package main.java.com.koupon.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(
    name = "coupon_issue",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "coupon_id"})  // (3) 중복 발급 방지
    }
)
public class CouponIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // (1) User와 N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // (2) Coupon과 N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private LocalDateTime issuedAt;
}
