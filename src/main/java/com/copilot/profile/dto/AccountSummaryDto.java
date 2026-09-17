package com.copilot.profile.dto;

import java.util.List;

public record AccountSummaryDto(
        String customerId,
        List<AccountDto> accounts,
        double totalBalanceUsd
) {
}
