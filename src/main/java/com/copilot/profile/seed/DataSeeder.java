package com.copilot.profile.seed;

import com.copilot.profile.domain.Account;
import com.copilot.profile.domain.ActivityEvent;
import com.copilot.profile.domain.CustomerProfile;
import com.copilot.profile.domain.Holding;
import com.copilot.profile.domain.RiskTolerance;
import com.copilot.profile.repository.AccountRepository;
import com.copilot.profile.repository.ActivityEventRepository;
import com.copilot.profile.repository.CustomerProfileRepository;
import com.copilot.profile.repository.HoldingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Seeds the in-memory H2 database with a handful of fictional customers so the
 * copilot has something to retrieve. This stands in for the "real" profile,
 * account, and activity microservices a production system would call instead.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final CustomerProfileRepository profileRepository;
    private final AccountRepository accountRepository;
    private final HoldingRepository holdingRepository;
    private final ActivityEventRepository activityRepository;

    public DataSeeder(CustomerProfileRepository profileRepository,
                       AccountRepository accountRepository,
                       HoldingRepository holdingRepository,
                       ActivityEventRepository activityRepository) {
        this.profileRepository = profileRepository;
        this.accountRepository = accountRepository;
        this.holdingRepository = holdingRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    public void run(String... args) {
        if (profileRepository.count() > 0) {
            return;
        }

        profileRepository.save(new CustomerProfile(
                "CUST1001", "John Carter", LocalDate.of(1985, 4, 12),
                "221B Baker Street, Boston, MA", "+1-617-555-0142", "john.carter@example.com",
                RiskTolerance.MODERATE, "VERIFIED"));

        profileRepository.save(new CustomerProfile(
                "CUST1002", "Priya Nair", LocalDate.of(1990, 11, 2),
                "48 Maple Ave, Austin, TX", "+1-512-555-0198", "priya.nair@example.com",
                RiskTolerance.AGGRESSIVE, "VERIFIED"));

        profileRepository.save(new CustomerProfile(
                "CUST1003", "Miguel Santos", LocalDate.of(1978, 7, 23),
                "9 Ocean View Dr, San Diego, CA", "+1-619-555-0173", "miguel.santos@example.com",
                RiskTolerance.CONSERVATIVE, "PENDING_REVIEW"));

        accountRepository.save(new Account("ACC-9001", "CUST1001", "BROKERAGE", 48250.75, "USD"));
        accountRepository.save(new Account("ACC-9002", "CUST1001", "IRA", 112300.10, "USD"));
        accountRepository.save(new Account("ACC-9003", "CUST1002", "BROKERAGE", 275400.00, "USD"));
        accountRepository.save(new Account("ACC-9004", "CUST1003", "IRA", 61900.40, "USD"));

        holdingRepository.save(new Holding("ACC-9001", "AAPL", 50, 9875.00));
        holdingRepository.save(new Holding("ACC-9001", "VTI", 120, 30120.00));
        holdingRepository.save(new Holding("ACC-9002", "VOO", 200, 92300.00));
        holdingRepository.save(new Holding("ACC-9003", "TSLA", 300, 67500.00));
        holdingRepository.save(new Holding("ACC-9003", "NVDA", 150, 207900.00));
        holdingRepository.save(new Holding("ACC-9004", "BND", 800, 61900.40));

        activityRepository.save(new ActivityEvent("CUST1001", "BENEFICIARY_CHANGE",
                "Primary beneficiary changed from spouse to family trust", LocalDate.now().minusDays(12)));
        activityRepository.save(new ActivityEvent("CUST1001", "TRADE",
                "Bought 20 shares of AAPL", LocalDate.now().minusDays(5)));
        activityRepository.save(new ActivityEvent("CUST1002", "TRADE",
                "Sold 100 shares of TSLA, bought 50 shares of NVDA", LocalDate.now().minusDays(3)));
        activityRepository.save(new ActivityEvent("CUST1002", "PROFILE_UPDATE",
                "Mailing address updated", LocalDate.now().minusDays(20)));
        activityRepository.save(new ActivityEvent("CUST1003", "LOGIN",
                "Logged in from a new device", LocalDate.now().minusDays(1)));
    }
}
