package com.studysync.dto;

import java.util.List;

/**
 * Mirrors the TypeScript {@code StudyPlan} interface from page.tsx:
 * <pre>
 * interface StudyPlan {
 *   examTitle: string;
 *   overview: string;
 *   syllabus: Syllabus[];
 *   schedule: Milestone[];
 *   tips: string[];
 * }
 * </pre>
 *
 * This is the shape Gemini is prompted to return, and the shape rendered
 * by the Thymeleaf result template.
 */
public record StudyPlan(
        String examTitle,
        String overview,
        List<Syllabus> syllabus,
        List<Milestone> schedule,
        List<String> tips
) {
}
