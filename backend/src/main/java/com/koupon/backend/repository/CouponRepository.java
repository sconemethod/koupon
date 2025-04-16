// koupon/backend/src/main/java/com/koupon/backend/repository/CouponRepository.java
package com.koupon.backend.repository;

import com.koupon.backend.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    // 필요한 쿼리 메소드 추가 가능
}
