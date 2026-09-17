package com.copilot.profile.service;

import com.copilot.profile.domain.Account;
import com.copilot.profile.dto.AccountDto;
import com.copilot.profile.dto.AccountSummaryDto;
import com.copilot.profile.dto.HoldingDto;
import com.copilot.profile.repository.AccountRepository;
import com.copilot.profile.repository.HoldingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Stands in for a "Customer Account" backend service (balances + holdings).
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final HoldingRepository holdingRepository;

    public AccountService(AccountRepository accountRepository, HoldingRepository holdingRepository) {
        this.accountRepository = accountRepository;
        this.holdingRepository = holdingRepository;
    }

    public AccountSummaryDto getAccountSummary(String customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);

        List<AccountDto> accountDtos = accounts.stream()
                .map(account -> new AccountDto(
                        account.getAccountId(),
                        account.getAccountType(),
                        account.getBalance(),
                        account.getCurrency(),
                        holdingRepository.findByAccountId(account.getAccountId()).stream()
                                .map(h -> new HoldingDto(h.getTicker(), h.getQuantity(), h.getMarketValue()))
                                .toList()
                ))
                .toList();

        double total = accounts.stream().mapToDouble(Account::getBalance).sum();

        return new AccountSummaryDto(customerId, accountDtos, total);
    }
}
