package com.studysync.dto;

import java.util.List;

/**
 * Mirrors the TypeScript {@code Syllabus} interface from page.tsx:
 * <pre>
 * interface Syllabus {
 *   subject: string;
 *   subtopics: string[];
 * }
 * </pre>
 */
public record Syllabus(String subject, List<String> subtopics) {
}
