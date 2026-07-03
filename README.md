# StudySync (Spring Boot + Thymeleaf)

Java Spring port of the original Next.js/React StudySync app. Generates an
AI study plan (via Gemini) for an exam, prep time, and proficiency level,
and renders it across four tabs: Overview, Syllabus, Timeline, Strategies.

## What changed vs. the original

- **Frontend**: React/JSX + client `useState` tab switching → Thymeleaf
  server-rendered HTML. All four tab panels are rendered in one page load;
  a small vanilla JS file (`static/js/tabs.js`) toggles which one is visible,
  reproducing the original's instant tab-switching without React.
- **Backend**: the Next.js API route (`api/generate-plan/route.ts`) →
  `PlanController` + `GeminiService`. Same prompt, same "extract JSON out of
  a possibly markdown-wrapped model response" recovery logic, same error
  messages.
- **Gemini call**: implemented as a direct REST call via Spring's
  `RestClient` (no Google SDK dependency) to keep the footprint small and
  the HTTP call inspectable.
- **Form flow**: the original posted JSON via `fetch` from React state. Here
  it's a standard HTML form POST to `/generate-plan`, validated server-side
  with Bean Validation (`@NotBlank` on exam name).

## Project layout

```
src/main/java/com/studysync/
  StudySyncApplication.java       - entry point
  config/GeminiProperties.java    - binds gemini.* config (api key, model, base URL)
  config/AppConfig.java           - RestClient bean
  controller/PlanController.java - GET / , POST /generate-plan
  service/GeminiService.java      - prompt building, Gemini call, JSON extraction
  service/PlanGenerationException.java
  dto/PlanRequest.java            - form-backing object
  dto/StudyPlan.java, Syllabus.java, Milestone.java  - mirrors the TS interfaces

src/main/resources/
  application.yml
  templates/index.html            - single template, all tabs
  static/css/style.css            - ported from globals.css + Planner.module.css
  static/js/tabs.js               - client-side tab switching
```

## Running it

Requires Java 17+ and Maven.

```bash
export GEMINI_API_KEY=your-key-here
mvn spring-boot:run
```

Then visit http://localhost:8080

## Note on verification

