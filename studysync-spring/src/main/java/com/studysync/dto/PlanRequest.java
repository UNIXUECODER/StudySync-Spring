package com.studysync.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Backs the "Generate Plan" form. Mirrors the request body previously sent as
 * JSON from the React client:
 * <pre>
 * { examName, timeAvailable, proficiency }
 * </pre>
 */
public class PlanRequest {

    @NotBlank(message = "Please tell us which exam you're preparing for.")
    private String examName;

    @NotBlank
    private String timeAvailable = "3 months";

    @NotBlank
    private String proficiency = "Beginner";

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getTimeAvailable() {
        return timeAvailable;
    }

    public void setTimeAvailable(String timeAvailable) {
        this.timeAvailable = timeAvailable;
    }

    public String getProficiency() {
        return proficiency;
    }

    public void setProficiency(String proficiency) {
        this.proficiency = proficiency;
    }
}
