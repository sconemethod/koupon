package com.koupon.backend.service;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;  
import com.koupon.backend.repository.EventRepository;
import com.koupon.backend.repository.CouponRepository;
import com.koupon.backend.repository.UserRepository;
import com.koupon.backend.domain.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;


@Service
@RequiredArgsConstructor

public class CouponService {

  private final RedisTemplate<String, Object> redisTemplate;
  private final DefaultRedisScript<Long> stockDecrementScript;
  private final KafkaProducerService kafkaProducerService;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final CouponRepository couponRepository;
  private final EventRepository eventRepository;
  private final UserRepository userRepository;



  /**
   * 현재 열려 있는 쿠폰 이벤트가 있는지 체크
   */
  public Event getActiveEvent() {
    LocalDateTime now = LocalDateTime.now();
    return eventRepository.findFirstByStartTimeBeforeAndEndTimeAfter(now, now)
        .orElseThrow(() -> new IllegalStateException("쿠폰 이벤트가 아직 시작되지 않았거나 종료되었습니다."));
  }

  /**
   * Redis 재고 차감 Lua 실행
   */
  public boolean tryIssueCouponFromRedis(String stockKey) {
    Long result = redisTemplate.execute(
        stockDecrementScript,
        Collections.singletonList(stockKey)
    );
    return result != null && result == 1L;
  }

  /**
   * 쿠폰 코드 생성 (8자리 랜덤 대문자)
   */
  public String generateCouponCode() {
    return UUID.randomUUID()
        .toString()
        .replace("-", "")
        .substring(0, 8)
        .toUpperCase();
  }

  /**
   * 쿠폰 발급 전체 흐름
   */
  public void issueCoupon(String userId) {
    // 1. 유저 조회
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

    // 2. 쿠폰 이벤트 조회
    Event event = getActiveEvent();

    // 3. 중복 발급 체크
    boolean alreadyIssued = couponRepository.existsByUser_UserIdAndEvent_EventId(userId, event.getEventId());
    if (alreadyIssued) {
        throw new IllegalStateException("이미 쿠폰을 발급 받았습니다.");
    }

    // 4. Redis 재고 차감
    String stockKey = "coupon_stock_event_" + event.getEventId();
    boolean success = tryIssueCouponFromRedis(stockKey);
    if (!success) {
        throw new IllegalStateException("쿠폰 재고가 모두 소진되었습니다.");
    }

    // 5. 쿠폰 코드 생성
    String couponCode = generateCouponCode();

    // 6. 쿠폰 저장
    Coupon coupon = Coupon.builder()
        .user(user)
        .event(event)
        .couponCode(couponCode)
        .issuedAt(LocalDateTime.now())
        .used("N")
        .build();   
    couponRepository.save(coupon);

    // 7. 이벤트 재고 차감
    event.setRemainingQuantity(event.getRemainingQuantity() - 1);
    eventRepository.save(event);

    // 8. Kafka 메시지 발행 (선택)
    kafkaProducerService.sendCouponIssueEvent(userId, couponCode);
  }

}
