# ROLA Module 22 Enterprise Education Platform

## Enterprise Platform Architecture Diagram

```text
Institution Admin / Teacher / Student / Parent
        |
        v
Enterprise Layer
  - RoleManager
  - InstitutionManager
  - ClassroomLearningMode
  - AssignmentManager
  - AITeachingAssistant
  - InstitutionAnalyticsEngine
  - LMSConnector
        |
        v
Existing ROLA AI Learning Platform
  - Adaptive Learning Engine -> Student Profile -> Teacher Dashboard
  - Knowledge Graph -> Lesson Recommendation
  - AI Tutor -> Student Support
  - Research Assistant -> Learning Materials
  - AR Scanner / 3D Visualization -> Classroom Activities
        |
        v
Cloud Backend
  - Firebase Auth custom claims
  - Firestore organization-isolated collections
  - Storage for learning materials and AR assets
  - Audit logs and analytics rollups
```

## Database Schema

Local Room tables:

```text
enterprise_roles
institutions
departments
courses
classes
members
assignments
submissions
classroom_sessions
teacher_analytics
student_reports
lms_connections
enterprise_audit_logs
```

Firestore collections:

```text
institutions
roles
departments
courses
classes
members
assignments
submissions
classroomSessions
teacherAnalytics
studentReports
lmsConnections
teacherDashboards
privacySettings
auditLogs
```

## Role Management Design

Supported roles:

- Super Admin: platform-wide management.
- Institution Admin: institution, department, course, class, teacher, student, and material management.
- Teacher: class management, assignments, submissions, class analytics, learning materials.
- Student: own progress and classroom session participation.
- Parent: child progress and recommended activities.

`RoleManager` evaluates permissions from cached role assignments and writes audit records for sensitive administration actions.

## Teacher Dashboard Design

Teacher dashboard shows:

- Classes.
- Assignments.
- Pending submission review count.
- Class analytics.
- AI-suggested learning materials.

Teachers can create classes, assign AR activities, review quiz/submission evidence, monitor progress, and use AI-assisted lesson planning through `AITeachingAssistant`.

## Student Dashboard Design

Student dashboard shows:

- Assigned activities.
- Learning progress.
- Completed lesson count.
- Quiz average.
- Achievements.
- Recommendations.

Student data integrates existing adaptive learning, quiz, gamification, and recommendation outputs into institution reporting.

## LMS Integration Strategy

`LMSConnector` prepares provider connections for:

- Moodle.
- Google Classroom.
- Microsoft Teams.
- Canvas LMS.

Initial integration is provider-neutral: connection metadata, sync status, and audit logging are modeled first. Provider OAuth, roster sync, assignment import/export, and grade passback should be added behind this connector without changing teacher/student dashboards.

## Classroom Learning Mode

```text
Teacher device
    -> Classroom session
    -> Students join
    -> Shared AR learning activity
    -> Group discussion and classroom challenge
    -> Assignment/submission/report update
```

## Enterprise Security

- Organization isolation by `institutionId`.
- Firebase custom claims for global role gating.
- Local `RoleManager` permissions for UI and workflow availability.
- Firestore rules for admin, teacher, student, and parent access paths.
- Audit logs for role, institution, class, assignment, submission, LMS, and session actions.
- Parent access limited to child reports and recommended activities.

## Performance Strategy

- Index classes by institution, course, and teacher.
- Index assignments by class and deadline.
- Index submissions by assignment, student, and status.
- Store analytics as summarized reports for dashboard reads.
- Keep classroom session documents small and append detailed event streams separately in future iterations.
- Sync LMS data incrementally by class/course scope.

## Testing Procedure

- Unit test role permissions for all supported roles.
- Unit test assignment creation, submission, and review permissions.
- Unit test AI lesson plan generation from knowledge graph and research materials.
- Integration test institution -> course -> class -> assignment -> submission.
- UI test teacher and student dashboards with empty and populated states.
- Security test Firestore access for super admin, institution admin, teacher, student, parent, and unauthenticated users.
- Load test large institution rosters, many classes, concurrent classroom sessions, and analytics report generation.
- LMS connector contract test for Moodle, Google Classroom, Microsoft Teams, and Canvas adapters.

## Enterprise Deployment Roadmap

1. Add Firebase custom claims for enterprise roles.
2. Seed institution admin accounts.
3. Import institution departments, courses, classes, teachers, students, and parents.
4. Validate organization isolation in staging.
5. Pilot teacher dashboards and classroom sessions with a small cohort.
6. Enable assignment workflows and parent reporting.
7. Connect LMS providers one institution at a time.
8. Add institution analytics dashboards and scheduled reports.
9. Expand to multi-domain curriculum templates.
10. Add SSO, rostering automation, grade passback, and compliance exports.
