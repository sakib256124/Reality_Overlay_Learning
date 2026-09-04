import {AuthContext, requireUser} from "../authentication/auth-middleware";
import {limitString} from "../api-gateway/http";

type Request = any;
type Response = any;
type FirebaseAdmin = any;

export async function handleAnalyticsRoute(
  firebaseAdmin: FirebaseAdmin,
  request: Request,
  response: Response,
  auth: AuthContext,
): Promise<void> {
  const uid = requireUser(auth);

  if (request.method === "POST") {
    const eventType = limitString(request.body?.eventType, "unknown", 80);
    await recordAnalyticsEvent(firebaseAdmin, uid, eventType, Date.now(), sanitizeMetrics(request.body?.metrics ?? {}));
    response.json({status: "ok"});
    return;
  }

  if (request.method === "GET") {
    const snapshot = await firebaseAdmin.firestore()
      .collection("analytics")
      .where("userId", "==", uid)
      .orderBy("timestamp", "desc")
      .limit(100)
      .get();
    response.json({events: snapshot.docs.map((document: any) => ({eventId: document.id, ...document.data()}))});
    return;
  }

  response.status(405).json({error: "Method not allowed"});
}

export async function recordAnalyticsEvent(
  firebaseAdmin: FirebaseAdmin,
  userId: string,
  eventType: string,
  timestamp: number,
  metrics: Record<string, unknown> = {},
): Promise<void> {
  const db = firebaseAdmin.firestore();
  const sanitizedMetrics = sanitizeMetrics(metrics);
  await db.collection("analytics").add({
    userId,
    eventType: limitString(eventType, "unknown", 80),
    timestamp,
    metrics: sanitizedMetrics,
  });

  await updateAnalyticsRollups(firebaseAdmin, userId, eventType, timestamp, sanitizedMetrics);
}

async function updateAnalyticsRollups(
  firebaseAdmin: FirebaseAdmin,
  userId: string,
  eventType: string,
  timestamp: number,
  metrics: Record<string, unknown>,
): Promise<void> {
  const db = firebaseAdmin.firestore();
  const fieldValue = firebaseAdmin.firestore.FieldValue;
  const day = new Date(timestamp).toISOString().slice(0, 10);
  const learningTimeSeconds = typeof metrics.learningTimeSeconds === "number" ? metrics.learningTimeSeconds : 0;
  const latencyMs = typeof metrics.latencyMs === "number" ? metrics.latencyMs : 0;

  await Promise.all([
    db.collection("learningAnalytics").doc(userId).set({
      userId,
      lastEventAt: timestamp,
      totalEvents: fieldValue.increment(1),
      totalLearningTimeSeconds: fieldValue.increment(learningTimeSeconds),
      [`eventCounts.${eventType}`]: fieldValue.increment(1),
      updatedAt: Date.now(),
    }, {merge: true}),
    db.collection("platformAnalytics").doc(day).set({
      windowId: day,
      windowStart: Date.parse(`${day}T00:00:00.000Z`),
      totalEvents: fieldValue.increment(1),
      totalLearningTimeSeconds: fieldValue.increment(learningTimeSeconds),
      totalLatencyMs: fieldValue.increment(latencyMs),
      [`eventCounts.${eventType}`]: fieldValue.increment(1),
      updatedAt: Date.now(),
    }, {merge: true}),
  ]);
}

function sanitizeMetrics(metrics: Record<string, unknown>): Record<string, unknown> {
  return Object.fromEntries(
    Object.entries(metrics)
      .slice(0, 30)
      .map(([key, value]) => [key.slice(0, 60), normalizeMetricValue(value)]),
  );
}

function normalizeMetricValue(value: unknown): string | number | boolean | null {
  if (typeof value === "string") return value.slice(0, 500);
  if (typeof value === "number" || typeof value === "boolean") return value;
  return null;
}
