package com.copilot.profile.agent;

import com.copilot.profile.dto.AccountSummaryDto;
import com.copilot.profile.dto.ActivityEventDto;
import com.copilot.profile.dto.CustomerProfileDto;
import com.copilot.profile.service.AccountService;
import com.copilot.profile.service.ActivityService;
import com.copilot.profile.service.CustomerProfileService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;

/**
 * The "tool belt" the LLM is allowed to use. Every public {@code @Tool}
 * method here maps 1:1 to a read-only backend call
 * The customerId always comes from the rep (via the request payload).
 */
public class ProfileTools {

    private final CustomerProfileService profileService;
    private final AccountService accountService;
    private final ActivityService activityService;
    private final List<String> trace;

    public ProfileTools(CustomerProfileService profileService,
                         AccountService accountService,
                         ActivityService activityService,
                         List<String> trace) {
        this.profileService = profileService;
        this.accountService = accountService;
        this.activityService = activityService;
        this.trace = trace;
    }

    @Tool(description = "Retrieve a customer's profile: address, phone, email, date of birth, " +
            "risk tolerance, and KYC status. Read-only.")
    public CustomerProfileDto getCustomerProfile(
            @ToolParam(description = "The unique customer ID, e.g. CUST1001") String customerId) {
        trace.add("getCustomerProfile(customerId=\"" + customerId + "\")");
        return profileService.getProfile(customerId);
    }

    @Tool(description = "Retrieve a customer's account summary: every account, its balance, " +
            "currency, and current holdings. Read-only.")
    public AccountSummaryDto getAccountSummary(
            @ToolParam(description = "The unique customer ID") String customerId) {
        trace.add("getAccountSummary(customerId=\"" + customerId + "\")");
        return accountService.getAccountSummary(customerId);
    }

    @Tool(description = "Retrieve a customer's recent activity - trades, beneficiary changes, " +
            "profile updates, and logins - within the last N days. Read-only.")
    public List<ActivityEventDto> getRecentActivity(
            @ToolParam(description = "The unique customer ID") String customerId,
            @ToolParam(description = "How many days to look back, e.g. 30") int days) {
        trace.add("getRecentActivity(customerId=\"" + customerId + "\", days=" + days + ")");
        return activityService.getRecentActivity(customerId, days);
    }

    public List<String> getTrace() {
        return trace;
    }
}
