package com.koupon.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

import com.koupon.backend.domain.Event;
import com.koupon.backend.repository.EventRepository;
import com.koupon.backend.dto.EventRequestDto;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;

    // 쿠폰 이벤트 생성
    @PostMapping("/event")
    public ResponseEntity<String> createEvent(@RequestBody EventRequestDto dto) {

        Event event = new Event();
        event.setEventName(dto.getEventName());
        event.setDescription(dto.getDescription());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setTotalQuantity(dto.getTotalQuantity());
        event.setRemainingQuantity(dto.getTotalQuantity()); // 초기 수량과 동일
        event.setCreatorUserId(dto.getCreatorUserId());
        event.setCreatedAt(LocalDateTime.now());

        eventRepository.save(event);

        return ResponseEntity.ok("쿠폰 이벤트가 성공적으로 생성되었습니다!");
    }
}
