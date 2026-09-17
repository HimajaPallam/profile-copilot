package com.copilot.profile.repository;

import com.copilot.profile.domain.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoldingRepository extends JpaRepository<Holding, Long> {

    List<Holding> findByAccountId(String accountId);
}
