package com.koupon.backend.repository;

import com.koupon.backend.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findFirstByStartTimeBeforeAndEndTimeAfter(LocalDateTime now1, LocalDateTime now2);

    // ← 여기부터 수정된 부분
    @Modifying
    @Transactional
    @Query(
      "UPDATE Event e " +
      "SET e.remainingQuantity = e.remainingQuantity - 1 " +
      "WHERE e.eventId = :eventId " +
      "  AND e.remainingQuantity > 0"
    )
    int decrementRemainingQuantity(@Param("eventId") Long eventId);
    // ↑ 반환값이 0 이면 차감실패(재고없음), 1 이면 정상 차감
}
