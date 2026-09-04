# ROLA AI Teacher Platform

Module 24 adds autonomous teaching and curriculum generation as a separate platform capability. The Android implementation lives under `app/src/main/java/com/rola/app/ai_teacher` and mirrors these service boundaries for future cloud deployment.

```text
Teacher Dashboard
    |
AI Teacher Engine
    |
+--- Curriculum Generator
+--- Lesson Generator
+--- Assessment Generator
+--- Adaptive Teaching Engine
+--- Classroom Assistant
+--- Quality Validator
    |
Knowledge Graph + Student Profile + Room Cache + Firestore Collections
```

## Responsibilities

- Generate curriculum structures from subject, topic, grade level, objective, duration, and learner level.
- Build lessons with objectives, explanations, examples, experiments, AR activities, and practice questions.
- Generate assessments across MCQ, practical, AR, and research formats.
- Adapt teaching style using learning history and quiz performance.
- Require teacher review before publishing generated content.
- Capture lesson effectiveness analytics for ongoing improvement.
