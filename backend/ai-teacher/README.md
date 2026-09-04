# AI Teacher Service

Cloud Functions handlers for autonomous curriculum drafts, lesson storage, assessment generation, teacher approval workflow, teaching plans, and lesson analytics.

Routes are mounted through `backend/api-gateway/router.ts`:

```text
POST /curriculums
GET  /curriculums
POST /lessons
POST /assessments
POST /teaching-plans
POST /teacher-reviews
POST /lesson-analytics
GET  /lesson-analytics?classId=
```

All writes require authenticated educator/admin claims. Student delivery should use approved or distributed curriculum records only.
