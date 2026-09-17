package com.copilot.profile.dto;

import java.util.List;

public record AccountDto(
        String accountId,
        String accountType,
        double balance,
        String currency,
        List<HoldingDto> holdings
) {
}
