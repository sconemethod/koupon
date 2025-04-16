// koupon/backend/src/main/java/com/koupon/backend/service/CouponService.java

package main.java.com.koupon.backend.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import main.java.com.koupon.backend.repository.CouponRepository;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public void issueCoupon(Long userId, Long couponId) {
        // Redis 확인, Kafka 발행, MySQL 저장 등 로직 들어올 예정
    }
}
