package com.copilot.profile.service;

import com.copilot.profile.dto.ActivityEventDto;
import com.copilot.profile.repository.ActivityEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Stands in for a "Customer Activity" backend service (trades, beneficiary
 * changes, profile edits, logins).
 */
@Service
public class ActivityService {

    private final ActivityEventRepository repository;

    public ActivityService(ActivityEventRepository repository) {
        this.repository = repository;
    }

    public List<ActivityEventDto> getRecentActivity(String customerId, int days) {
        LocalDate since = LocalDate.now().minusDays(days);
        return repository.findByCustomerIdAndEventDateGreaterThanEqualOrderByEventDateDesc(customerId, since)
                .stream()
                .map(e -> new ActivityEventDto(e.getEventType(), e.getDescription(), e.getEventDate()))
                .toList();
    }
}
