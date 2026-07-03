package com.studysync.service;

/**
 * Thrown when a study plan could not be generated, e.g. missing API key,
 * a failed call to Gemini, or a response that couldn't be parsed as the
 * expected JSON shape.
 *
 * Mirrors the error-message behavior of the original route.ts, which caught
 * any thrown error and surfaced its message (or a generic fallback) to the
 * client.
 */
public class PlanGenerationException extends RuntimeException {

    public PlanGenerationException(String message) {
        super(message);
    }

    public PlanGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
