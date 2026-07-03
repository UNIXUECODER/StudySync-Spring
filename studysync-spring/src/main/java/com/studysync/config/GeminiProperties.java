package com.studysync.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds the {@code gemini.*} properties from application.yml / env vars.
 * Equivalent of reading {@code process.env.GEMINI_API_KEY} in route.ts.
 */
@ConfigurationProperties(prefix = "gemini")
public class GeminiProperties {

    /** API key for the Gemini API. Empty/blank if not configured. */
    private String apiKey = "";

    /** Model name, matches the "gemini-2.5-flash-lite" used in route.ts. */
    private String model = "gemini-2.5-flash-lite";

    /** Base URL for the Generative Language API. */
    private String baseUrl = "https://generativelanguage.googleapis.com";

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }
}
