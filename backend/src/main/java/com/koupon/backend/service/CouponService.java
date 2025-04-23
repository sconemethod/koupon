package com.koupon.backend.service;

import com.koupon.backend.domain.Coupon;
import com.koupon.backend.domain.Event;
import com.koupon.backend.domain.User;
import com.koupon.backend.repository.CouponRepository;
import com.koupon.backend.repository.EventRepository;
import com.koupon.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.micrometer.core.instrument.MeterRegistry;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {
  private final MeterRegistry meterRegistry;
  private final DefaultRedisScript<Long> stockDecrementScript;
  private final RedisTemplate<String, String> redisTemplate;
  private final UserRepository userRepository;
  private final EventRepository eventRepository;
  private final CouponRepository couponRepository;
  private final KafkaProducerService kafkaProducerService;

  @Transactional
  public Coupon issueCoupon(String userId, Long eventId) {
    // 0. 카운터 증가
    meterRegistry.counter("coupon.issued.attempts").increment();

    try {
      // 1. 유저 & 이벤트 유효성 체크
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
      Event event = eventRepository.findById(eventId)
          .orElseThrow(() -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다."));
      LocalDateTime now = LocalDateTime.now();
      if (now.isBefore(event.getStartTime()) || now.isAfter(event.getEndTime())) {
        throw new IllegalStateException("이 이벤트는 아직 시작되지 않았거나 종료되었습니다.");
      }

      // 2. 중복 발급 체크
      if (couponRepository.existsByUser_UserIdAndEvent_EventId(userId, eventId)) {
        throw new IllegalStateException("이미 쿠폰을 발급받았습니다.");
      }

      // 3. Redis 재고 차감
      String stockKey = "coupon_stock_event_" + eventId;
      Long redisResult = redisTemplate.execute(stockDecrementScript, Collections.singletonList(stockKey));
      if (redisResult == null || redisResult < 0) {
        throw new IllegalStateException("쿠폰 재고가 없습니다.");
      }

      // 4. DB 재고 원자적 차감
      int updated = eventRepository.decrementRemainingQuantity(eventId);
      if (updated == 0) {
        // DB에 재고 없으면 Redis 롤백
        redisTemplate.opsForValue().increment(stockKey);
        throw new IllegalStateException("이벤트 재고가 소진되었습니다.");
      }

      // 5. 쿠폰 생성 & 저장
      String couponCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
      Coupon coupon = new Coupon();
      coupon.setUser(user);
      coupon.setEvent(event);
      coupon.setCouponCode(couponCode);
      coupon.setIssuedAt(now);
      coupon.setUsed("N");
      couponRepository.save(coupon);


      // 6. Kafka 메시지 발송
      kafkaProducerService.sendCouponIssueEvent(userId, couponCode);

      // 7. 성공 카운터 증가
      meterRegistry.counter("coupon.issued.success").increment();
      return coupon;
  } catch (Exception ex) {
    // 실패 시
      meterRegistry.counter("coupon.issued.failures").increment();
      throw ex;  // 트랜잭션 롤백을 위해 예외 재던짐
    }
  }
}
