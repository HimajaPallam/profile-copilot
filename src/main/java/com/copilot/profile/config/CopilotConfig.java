package com.copilot.profile.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CopilotConfig {

    /**
     * spring-ai-starter-model-ollama auto-configures a ChatClient.Builder
     * backed by the local Ollama model declared in application.yml. We just
     * build the ChatClient itself here.
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}
