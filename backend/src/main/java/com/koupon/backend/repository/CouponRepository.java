// koupon/backend/src/main/java/com/koupon/backend/repository/CouponRepository.java

package main.java.com.koupon.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import main.java.com.koupon.backend.domain.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    // 추후 findByName 등 메서드 추가 가능
}