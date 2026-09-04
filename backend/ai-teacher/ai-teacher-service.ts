import {AuthContext, requireAdmin, requireUser} from "../authentication/auth-middleware";
import {arrayOfStrings, limitString, sanitizeId} from "../api-gateway/http";
import {recordAnalyticsEvent} from "../analytics/analytics-service";

type Request = any;
type Response = any;
type FirebaseAdmin = any;

export async function handleAiTeacherRoute(
  firebaseAdmin: FirebaseAdmin,
  request: Request,
  response: Response,
  auth: AuthContext,
): Promise<void> {
  const uid = requireUser(auth);
  const db = firebaseAdmin.firestore();
  const path = request.path.replace(/^\/api/, "");
  const startedAt = Date.now();

  if (path.startsWith("/curriculums")) {
    requireAdmin(auth);
    if (request.method === "GET") {
      const teacherId = limitString(request.query?.teacherId, uid, 100);
      const snapshot = await db.collection("curriculums")
        .where("teacherId", "==", teacherId)
        .orderBy("updatedAt", "desc")
        .limit(50)
        .get();
      response.json({curriculums: snapshot.docs.map((doc: any) => ({curriculumId: doc.id, ...doc.data()}))});
      return;
    }
    if (request.method === "POST") {
      const curriculum = buildCurriculumDraft(uid, request.body);
      await db.collection("curriculums").doc(curriculum.curriculumId).set(curriculum, {merge: true});
      await recordAnalyticsEvent(firebaseAdmin, uid, "ai_curriculum_generated", startedAt, {
        curriculumId: curriculum.curriculumId,
        topic: curriculum.topic,
        latencyMs: Date.now() - startedAt,
      });
      response.status(201).json(curriculum);
      return;
    }
  }

  if (path.startsWith("/lessons") && request.method === "POST") {
    requireAdmin(auth);
    const lesson = buildLesson(uid, request.body);
    await db.collection("lessons").doc(lesson.lessonId).set(lesson, {merge: true});
    response.status(201).json(lesson);
    return;
  }

  if (path.startsWith("/assessments") && request.method === "POST") {
    requireAdmin(auth);
    const assessment = buildAssessment(uid, request.body);
    await db.collection("assessments").doc(assessment.assessmentId).set(assessment, {merge: true});
    response.status(201).json(assessment);
    return;
  }

  if (path.startsWith("/teaching-plans") && request.method === "POST") {
    requireAdmin(auth);
    const plan = {
      planId: `teaching_plan_${Date.now()}`,
      teacherId: limitString(request.body?.teacherId, uid, 100),
      studentLevel: limitString(request.body?.studentLevel, "Beginner", 24),
      explanationComplexity: limitString(request.body?.explanationComplexity, "simple, visual, step-by-step", 240),
      learningSpeed: limitString(request.body?.learningSpeed, "Balanced", 40),
      recommendedInterventions: arrayOfStrings(request.body?.recommendedInterventions, 12, 160),
      createdBy: uid,
      updatedAt: Date.now(),
    };
    await db.collection("teachingPlans").doc(plan.planId).set(plan, {merge: true});
    response.status(201).json(plan);
    return;
  }

  if (path.startsWith("/teacher-reviews") && request.method === "POST") {
    requireAdmin(auth);
    const status = limitString(request.body?.status, "PendingReview", 40);
    const review = {
      reviewId: `teacher_review_${Date.now()}`,
      contentId: sanitizeId(request.body?.contentId, "content"),
      teacherId: limitString(request.body?.teacherId, uid, 100),
      status,
      comments: limitString(request.body?.comments, "", 1200),
      reviewedAt: Date.now(),
    };
    await db.collection("teacherReviews").doc(review.reviewId).set(review, {merge: true});
    await db.collection("curriculums").doc(review.contentId).set({approvalStatus: status, updatedAt: Date.now()}, {merge: true});
    response.status(201).json(review);
    return;
  }

  if (path.startsWith("/lesson-analytics")) {
    requireAdmin(auth);
    if (request.method === "GET") {
      const classId = limitString(request.query?.classId, "", 100);
      const query = classId ?
        db.collection("lessonAnalytics").where("classId", "==", classId).orderBy("generatedAt", "desc").limit(50) :
        db.collection("lessonAnalytics").orderBy("generatedAt", "desc").limit(50);
      const snapshot = await query.get();
      response.json({analytics: snapshot.docs.map((doc: any) => ({analyticsId: doc.id, ...doc.data()}))});
      return;
    }
    if (request.method === "POST") {
      const analytics = {
        analyticsId: `lesson_analytics_${Date.now()}`,
        lessonId: sanitizeId(request.body?.lessonId, "lesson"),
        classId: sanitizeId(request.body?.classId, "class"),
        completionRate: clampPercent(request.body?.completionRate),
        engagementScore: clampPercent(request.body?.engagementScore),
        averageAssessmentScore: clampPercent(request.body?.averageAssessmentScore),
        difficultySignal: limitString(request.body?.difficultySignal, "Beginner", 24),
        improvementNotes: arrayOfStrings(request.body?.improvementNotes, 10, 180),
        generatedAt: Date.now(),
      };
      await db.collection("lessonAnalytics").doc(analytics.analyticsId).set(analytics, {merge: true});
      response.status(201).json(analytics);
      return;
    }
  }

  response.status(404).json({error: "AI teacher route not found"});
}

function buildCurriculumDraft(uid: string, body: any): Record<string, unknown> {
  const topic = limitString(body?.topic, "General Science", 120);
  const durationWeeks = Math.max(1, Math.min(Number(body?.durationWeeks ?? 1), 12));
  const curriculumId = sanitizeId(body?.curriculumId, `curriculum_${Date.now()}`);
  return {
    curriculumId,
    teacherId: limitString(body?.teacherId, uid, 100),
    institutionId: limitString(body?.institutionId, "local-institution", 100),
    subject: limitString(body?.subject, "Science", 80),
    topic,
    gradeLevel: limitString(body?.gradeLevel, "General", 80),
    learningObjective: limitString(body?.learningObjective, `Understand ${topic}`, 240),
    durationWeeks,
    studentLevel: limitString(body?.studentLevel, "Beginner", 24),
    languageCode: limitString(body?.languageCode, "en", 12),
    title: limitString(body?.title, `${topic} Curriculum`, 160),
    overview: limitString(body?.overview, `A ${durationWeeks}-week AI-assisted curriculum for ${topic}.`, 600),
    approvalStatus: "Draft",
    quality: {
      scientificAccuracy: 0.8,
      levelAlignment: 0.8,
      objectiveCoverage: 0.8,
      contentConsistency: 0.8,
    },
    createdBy: uid,
    createdAt: Date.now(),
    updatedAt: Date.now(),
  };
}

function buildLesson(uid: string, body: any): Record<string, unknown> {
  const topic = limitString(body?.topic, "Science Concept", 120);
  return {
    lessonId: sanitizeId(body?.lessonId, `lesson_${Date.now()}`),
    curriculumId: sanitizeId(body?.curriculumId, "curriculum"),
    title: limitString(body?.title, topic, 160),
    objectives: arrayOfStrings(body?.objectives, 8, 180),
    explanation: limitString(body?.explanation, `${topic} explained with observable evidence and AR exploration.`, 2000),
    examples: arrayOfStrings(body?.examples, 8, 180),
    experiments: arrayOfStrings(body?.experiments, 8, 180),
    arActivityIds: arrayOfStrings(body?.arActivityIds, 12, 100),
    practiceQuestions: arrayOfStrings(body?.practiceQuestions, 12, 180),
    difficulty: limitString(body?.difficulty, "Beginner", 24),
    createdBy: uid,
    updatedAt: Date.now(),
  };
}

function buildAssessment(uid: string, body: any): Record<string, unknown> {
  const topic = limitString(body?.topic, "Science Concept", 120);
  return {
    assessmentId: sanitizeId(body?.assessmentId, `assessment_${Date.now()}`),
    curriculumId: sanitizeId(body?.curriculumId, "curriculum"),
    title: limitString(body?.title, `${topic} Assessment`, 160),
    difficulty: limitString(body?.difficulty, "Beginner", 24),
    mcqQuestions: arrayOfStrings(body?.mcqQuestions, 20, 240),
    practicalTasks: arrayOfStrings(body?.practicalTasks, 10, 240),
    arAssignments: arrayOfStrings(body?.arAssignments, 10, 240),
    researchActivities: arrayOfStrings(body?.researchActivities, 10, 240),
    createdBy: uid,
    updatedAt: Date.now(),
  };
}

function clampPercent(value: unknown): number {
  return Math.max(0, Math.min(Number(value ?? 0), 100));
}
