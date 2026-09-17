package com.copilot.profile.repository;

import com.copilot.profile.domain.ActivityEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ActivityEventRepository extends JpaRepository<ActivityEvent, Long> {

    List<ActivityEvent> findByCustomerIdAndEventDateGreaterThanEqualOrderByEventDateDesc(
            String customerId, LocalDate since);
}
