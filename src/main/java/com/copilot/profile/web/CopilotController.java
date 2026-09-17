package com.copilot.profile.web;

import com.copilot.profile.agent.ProfileTools;
import com.copilot.profile.service.AccountService;
import com.copilot.profile.service.ActivityService;
import com.copilot.profile.service.CustomerProfileService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/copilot")
public class CopilotController {

    private static final String SYSTEM_PROMPT = """
            You are a read-only customer profile assistant for a brokerage's internal support team.
            You can look up a customer's profile, account summary, and recent activity using the tools
            provided. You have NO ability to modify, update, or delete any customer data - you are
            strictly retrieval-only, and you must never claim to have made a change.

            Every request tells you exactly which customerId the rep is asking about - always pass
            that exact customerId to the tools. Never guess, infer, or substitute a different
            customerId, and never resolve identity from a name.

            Call only the tools needed to answer the question - for example, don't fetch account data
            if the rep only asked about risk tolerance. Answer concisely and only with information
            returned by the tools. If a tool returns no data, or the customerId does not exist, say so
            plainly rather than inventing an answer.
            """;

    private final ChatClient chatClient;
    private final CustomerProfileService profileService;
    private final AccountService accountService;
    private final ActivityService activityService;

    public CopilotController(ChatClient chatClient,
                              CustomerProfileService profileService,
                              AccountService accountService,
                              ActivityService activityService) {
        this.chatClient = chatClient;
        this.profileService = profileService;
        this.accountService = accountService;
        this.activityService = activityService;
    }

    public record AskRequest(String customerId, String question) {
    }

    public record AskResponse(String answer, List<String> toolTrace) {
    }

    @PostMapping("/ask")
    public AskResponse ask(@RequestBody AskRequest request) {
        List<String> trace = new ArrayList<>();
        ProfileTools tools = new ProfileTools(profileService, accountService, activityService, trace);

        String userMessage = "customerId: " + request.customerId() + "\nQuestion: " + request.question();

        String answer = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(userMessage)
                .tools(tools)
                .call()
                .content();

        return new AskResponse(answer, trace);
    }
}
