package com.koupon.backend.repository;

import com.koupon.backend.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findFirstByStartTimeBeforeAndEndTimeAfter(LocalDateTime startTime, LocalDateTime endTime);
}

