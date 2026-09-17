package com.copilot.profile.dto;

import java.time.LocalDate;

public record CustomerProfileDto(
        String customerId,
        String fullName,
        LocalDate dateOfBirth,
        String address,
        String phone,
        String email,
        String riskTolerance,
        String kycStatus
) {
}
