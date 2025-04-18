package com.koupon.backend.repository;

import com.koupon.backend.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
  

// 쿠폰 ID로 쿠폰 정보 조회
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    boolean existsByUser_UserIdAndEvent_EventId(String userId, Long eventId);
}
