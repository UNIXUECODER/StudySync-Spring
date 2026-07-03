package com.studysync.controller;

import com.studysync.dto.PlanRequest;
import com.studysync.dto.StudyPlan;
import com.studysync.service.GeminiService;
import com.studysync.service.PlanGenerationException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Server-rendered replacement for page.tsx + api/generate-plan/route.ts.
 *
 * The original was a client-rendered SPA: one page, four tabs swapped via
 * React state, backed by a JSON API route. Here everything is rendered in a
 * single Thymeleaf template (index.html); once a plan exists all four tabs'
 * content is present in the DOM and a small vanilla-JS snippet (tabs.js)
 * handles switching between them client-side, exactly as the React version
 * did, without a full page reload.
 */
@Controller
public class PlanController {

    private final GeminiService geminiService;

    public PlanController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @GetMapping("/")
    public String index(Model model) {
        if (!model.containsAttribute("planRequest")) {
            model.addAttribute("planRequest", new PlanRequest());
        }
        model.addAttribute("activeTab", "generate");
        return "index";
    }

    @PostMapping("/generate-plan")
    public String generatePlan(@Valid @ModelAttribute("planRequest") PlanRequest planRequest,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activeTab", "generate");
            return "index";
        }

        try {
            StudyPlan plan = geminiService.generatePlan(planRequest);
            model.addAttribute("plan", plan);
            model.addAttribute("activeTab", "overview");
        } catch (PlanGenerationException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("activeTab", "generate");
        }

        return "index";
    }
}
