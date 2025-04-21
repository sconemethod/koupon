package com.koupon.backend.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import com.koupon.backend.domain.Event;
import com.koupon.backend.repository.EventRepository;
import com.koupon.backend.dto.EventRequestDto;
import java.util.Optional;
import java.time.LocalDateTime;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;
    private final RedisTemplate<String, String> redisTemplate;
    // 쿠폰 이벤트 생성
     // ✅ 관리자용: 이벤트 생성
     @PostMapping("/admin/event")
     public ResponseEntity<String> createEvent(@RequestBody EventRequestDto dto) {
         Event event = new Event();
         event.setEventName(dto.getEventName());
         event.setDescription(dto.getDescription());
         event.setStartTime(dto.getStartTime());
         event.setEndTime(dto.getEndTime());
         event.setTotalQuantity(dto.getTotalQuantity());
         event.setRemainingQuantity(dto.getTotalQuantity());
         event.setCreatorUserId(dto.getCreatorUserId());
         event.setCreatedAt(LocalDateTime.now());
 
         eventRepository.save(event);
 
         // Redis 재고 초기화
         String redisKey = "coupon_stock_event_" + event.getEventId();
         redisTemplate.opsForValue().set(redisKey, String.valueOf(event.getTotalQuantity()));
 
         return ResponseEntity.ok("쿠폰 이벤트가 성공적으로 생성되었습니다!");
     }

     // ✅ 관리자용: 이벤트 수정
     @PutMapping("/admin/event/{id}")
     public ResponseEntity<String> updateEvent(@PathVariable Long id, @RequestBody EventRequestDto dto) {
         Optional<Event> optionalEvent = eventRepository.findById(id);
         if (optionalEvent.isEmpty()) {
             return ResponseEntity.notFound().build();
         }
     
         Event event = optionalEvent.get();
     
         // ✅ 기존 발급 수량 계산
         int issued = event.getTotalQuantity() - event.getRemainingQuantity();
     
         // ✅ 필드 업데이트
         event.setDescription(dto.getDescription());
         event.setStartTime(dto.getStartTime());
         event.setEndTime(dto.getEndTime());
         event.setTotalQuantity(dto.getTotalQuantity());
     
         // ✅ 남은 수량 재계산 (최소 0 보장)
         int newRemaining = Math.max(dto.getTotalQuantity() - issued, 0);
         event.setRemainingQuantity(newRemaining);
     
         eventRepository.save(event);
     
         return ResponseEntity.ok("이벤트가 성공적으로 수정되었습니다!");
     }
     




 
     // ✅ 사용자용: 전체 이벤트 조회
     @GetMapping("/events")
     public List<Event> getAllEvents() {
         return eventRepository.findAll();
     }
 
     // ✅ 사용자용: 이벤트 상세 조회
     @GetMapping("/events/{id}")
     public ResponseEntity<Event> getEventById(@PathVariable Long id) {
         return eventRepository.findById(id)
             .map(ResponseEntity::ok)
             .orElse(ResponseEntity.notFound().build());
     }
 }