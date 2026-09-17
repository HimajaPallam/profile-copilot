package com.copilot.profile.repository;

import com.copilot.profile.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {

    List<Account> findByCustomerId(String customerId);
}
