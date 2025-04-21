package com.koupon.backend.service;

import com.koupon.backend.domain.Coupon;
import com.koupon.backend.domain.Event;
import com.koupon.backend.domain.User;
import com.koupon.backend.repository.CouponRepository;
import com.koupon.backend.repository.EventRepository;
import com.koupon.backend.repository.UserRepository;
import com.koupon.backend.service.KafkaProducerService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.kafka.core.KafkaTemplate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final DefaultRedisScript<Long> stockDecrementScript;
    private final KafkaProducerService kafkaProducerService;
    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    private final RedisTemplate<String, String> redisTemplate;
    public Coupon issueCoupon(String userId, Long eventId) {
        // 1. 유저 조회
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        // 2. 현재 열려 있는 이벤트 찾기
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다."));

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(event.getStartTime()) || now.isAfter(event.getEndTime())) {
            throw new IllegalStateException("이 이벤트는 아직 시작되지 않았거나 종료되었습니다.");
        }

    

        // 3. 중복 발급 체크
        boolean alreadyIssued = couponRepository.existsByUser_UserIdAndEvent_EventId(userId, event.getEventId());
        if (alreadyIssued) {
            throw new IllegalStateException("이미 쿠폰을 발급받았습니다.");
        }

        // 4. Redis 재고 차감
        String stockKey = "coupon_stock_event_" + event.getEventId();
        Long result = redisTemplate.execute(stockDecrementScript, Collections.singletonList(stockKey));
        if (result == null || result <= 0) {
            throw new IllegalStateException("쿠폰 재고가 없습니다.");
        }

        // 5. 쿠폰 코드 생성
        String couponCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 6. 쿠폰 저장
        Coupon coupon = new Coupon();
        coupon.setUser(user);
        coupon.setEvent(event);
        coupon.setCouponCode(couponCode);
        coupon.setIssuedAt(LocalDateTime.now());
        coupon.setUsed("N");
        couponRepository.save(coupon);

        // 7. Kafka 메시지 전송
        kafkaProducerService.sendCouponIssueEvent(userId, couponCode);

        // 8. Redis 재고 차감 성공 후 → DB에서도 반영
        event.setRemainingQuantity(event.getRemainingQuantity() - 1);
        eventRepository.save(event);


        return coupon;
    }
}
