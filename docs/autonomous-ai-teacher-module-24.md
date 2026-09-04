# Module 24: Autonomous AI Teacher and Curriculum Generator

## Architecture Diagram

```text
Teacher Dashboard (Compose + MVVM)
        |
        v
AITeacherDashboardViewModel
        |
        v
AITeacherEngine
   |         |            |              |
   v         v            v              v
Curriculum  Lesson       Assessment     AdaptiveTeaching
Generator   Generator    Generator      Engine
   |                         |              |
   v                         v              v
Knowledge Graph ----> Concept Relationships ----> Learning Path Signals
        |
        v
AITeacherRepository
        |
        v
Room DAO / Firebase Sync Boundary
```

## Curriculum Generation Workflow

```text
Subject + Topic + Grade + Objective + Duration + Student Level
        |
        v
Knowledge graph semantic search
        |
        v
Concept ordering and module planning
        |
        v
Lesson, AR activity, experiment, and assessment generation
        |
        v
Quality validation
        |
        v
Draft content
        |
        v
Teacher review
        |
        v
Approved / Rejected
        |
        v
Student distribution
```

## Database Design

The local Room schema maps the required education collections to scalable offline-first tables:

- `ai_curriculums`: course/curriculum draft, ownership, institution scope, quality scores, approval status.
- `curriculum_modules`: ordered course modules and linked lessons.
- `lessons`: generated lesson objectives, explanations, examples, experiments, AR activity references, and practice checks.
- `activities`: AR planning activities for scanning, 3D exploration, virtual experiments, and collaboration.
- `assessments`: MCQ, practical tasks, AR assignments, and research activities.
- `ai_teaching_plans`: adaptive teacher plans from learning history and quiz signals.
- `teacher_reviews`: approval workflow and teacher feedback.
- `lesson_analytics`: lesson effectiveness, engagement, difficulty, and improvement signals.

Firestore collection names should mirror the product language for backend sync:

- `courses`
- `curriculums`
- `lessons`
- `activities`
- `assessments`
- `teachingPlans`
- `teacherFeedback`
- `aiGeneratedContent`
- `aiTeacherAuditHistory`

## Teacher Dashboard Design

The dashboard supports:

- Curriculum draft generation from subject, topic, grade, objective, duration, and student level.
- Teaching plan generation for weekly sequencing and teacher materials.
- Adaptive plan generation from learning profile signals.
- AI content review queue.
- Approval and student distribution actions.
- Lesson, curriculum, student insight, and improvement analytics summaries.

## Security Model

AI Teacher actions use `TeacherSecurityContext` and permission checks:

- `GenerateCurriculum`
- `ReviewContent`
- `ApproveContent`
- `DistributeContent`
- `ViewStudentAnalytics`

Curriculum ownership stays scoped by `teacherId`, `ownerUserId`, and `institutionId`. Audit-friendly records are represented by `AITeacherAuditRecord` and generated content metadata by `AIGeneratedContentRecord`.

## Testing Procedure

1. Run focused unit tests:

   ```bash
   ./gradlew :app:testDevDebugUnitTest --tests "com.rola.app.unit.AITeacherSystemTest"
   ```

2. Run all local unit tests:

   ```bash
   ./gradlew :app:testDevDebugUnitTest
   ```

3. Validate curriculum generation:

   - Generated curriculum contains modules, two lessons per module, AR activities, experiments, and assessments.
   - Quality report is publish-ready only when objectives, assessment coverage, AR, and reviewable content are present.

4. Validate teacher workflow:

   - Draft content can move to pending review.
   - Teacher approval creates a review record and updates status.
   - Distribution requires distribute permission.

5. Validate adaptive teaching:

   - Quiz score, weak areas, difficult concepts, and learning speed produce interventions, practice, and recommendations.

## Future AI Education Roadmap

- Add rubric-aware assessment scoring and feedback generation.
- Add institution-level curriculum standards alignment.
- Add teacher-edit diff history for every AI-generated lesson.
- Add multi-student cohort analytics and small-group recommendations.
- Add model evaluation gates for hallucination, reading level, bias, and accessibility.
- Add localized curriculum templates for national and regional standards.
- Add collaborative teacher review with department approval chains.
