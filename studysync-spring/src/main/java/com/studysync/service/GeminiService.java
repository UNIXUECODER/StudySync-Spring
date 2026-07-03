package com.studysync.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studysync.config.GeminiProperties;
import com.studysync.dto.PlanRequest;
import com.studysync.dto.StudyPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java port of the logic in src/app/api/generate-plan/route.ts.
 *
 * Builds the same prompt, calls the Gemini "generateContent" REST endpoint
 * directly (no SDK dependency), and applies the same "extract JSON out of a
 * possibly markdown-wrapped response" recovery step before parsing.
 */
@Service
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);

    // Mirrors the TS regex /\{[\s\S]*\}/ - the first "{" through the last "}".
    private static final Pattern JSON_BLOCK = Pattern.compile("\\{[\\s\\S]*\\}");

    private final RestClient restClient;
    private final GeminiProperties properties;
    private final ObjectMapper objectMapper;

    public GeminiService(RestClient geminiRestClient, GeminiProperties properties, ObjectMapper objectMapper) {
        this.restClient = geminiRestClient;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public StudyPlan generatePlan(PlanRequest request) {
        if (!properties.isConfigured()) {
            throw new PlanGenerationException("Gemini API key is not configured.");
        }

        String prompt = buildPrompt(request);

        String rawText;
        try {
            rawText = callGemini(prompt);
        } catch (RestClientException ex) {
            log.error("Error generating study plan", ex);
            throw new PlanGenerationException("Failed to generate study plan. Please try again.", ex);
        }

        String jsonString = extractJson(rawText);

        try {
            return objectMapper.readValue(jsonString, StudyPlan.class);
        } catch (Exception ex) {
            log.error("Error generating study plan", ex);
            throw new PlanGenerationException("Failed to generate study plan. Please try again.", ex);
        }
    }

    private String buildPrompt(PlanRequest request) {
        return """
                Create a detailed study plan for the following exam:
                Exam: %s
                Preparation Time: %s
                Current Proficiency: %s

                Please provide the response in a structured JSON format with the following keys:
                - "examTitle": The full name of the exam.
                - "overview": A brief summary of the preparation strategy.
                - "syllabus": An array of objects, each with "subject" and "subtopics" (array of strings).
                - "schedule": An array of "milestones", each with "timeframe" (e.g., Week 1) and "goals" (array of strings).
                - "tips": An array of general study tips for this specific exam.

                Ensure the JSON is valid and the content is accurate for the %s exam.
                """.formatted(request.getExamName(), request.getTimeAvailable(), request.getProficiency(), request.getExamName());
    }

    private String callGemini(String prompt) {
        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );

        JsonNode response = restClient.post()
                .uri("/v1beta/models/{model}:generateContent?key={apiKey}",
                        properties.getModel(), properties.getApiKey())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(JsonNode.class);

        if (response == null) {
            throw new PlanGenerationException("Empty response from Gemini API.");
        }

        JsonNode textNode = response.path("candidates").path(0)
                .path("content").path("parts").path(0).path("text");

        if (textNode.isMissingNode() || textNode.asText().isBlank()) {
            throw new PlanGenerationException("Gemini API returned no content.");
        }

        return textNode.asText();
    }

    /**
     * Equivalent of:
     * const jsonMatch = text.match(/\{[\s\S]*\}/);
     * const jsonString = jsonMatch ? jsonMatch[0] : text;
     */
    private String extractJson(String text) {
        Matcher matcher = JSON_BLOCK.matcher(text);
        return matcher.find() ? matcher.group() : text;
    }
}
