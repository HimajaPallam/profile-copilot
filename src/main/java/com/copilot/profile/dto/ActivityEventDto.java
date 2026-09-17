package com.copilot.profile.dto;

import java.time.LocalDate;

public record ActivityEventDto(String eventType, String description, LocalDate eventDate) {
}
