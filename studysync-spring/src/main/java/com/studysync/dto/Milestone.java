package com.studysync.dto;

import java.util.List;

/**
 * Mirrors the TypeScript {@code Milestone} interface from page.tsx:
 * <pre>
 * interface Milestone {
 *   timeframe: string;
 *   goals: string[];
 * }
 * </pre>
 */
public record Milestone(String timeframe, List<String> goals) {
}
