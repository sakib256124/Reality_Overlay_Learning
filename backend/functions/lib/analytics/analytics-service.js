"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.handleAnalyticsRoute = handleAnalyticsRoute;
exports.recordAnalyticsEvent = recordAnalyticsEvent;
const auth_middleware_1 = require("../authentication/auth-middleware");
const http_1 = require("../api-gateway/http");
async function handleAnalyticsRoute(firebaseAdmin, request, response, auth) {
    const uid = (0, auth_middleware_1.requireUser)(auth);
    if (request.method === "POST") {
        const eventType = (0, http_1.limitString)(request.body?.eventType, "unknown", 80);
        await recordAnalyticsEvent(firebaseAdmin, uid, eventType, Date.now(), sanitizeMetrics(request.body?.metrics ?? {}));
        response.json({ status: "ok" });
        return;
    }
    if (request.method === "GET") {
        const snapshot = await firebaseAdmin.firestore()
            .collection("analytics")
            .where("userId", "==", uid)
            .orderBy("timestamp", "desc")
            .limit(100)
            .get();
        response.json({ events: snapshot.docs.map((document) => ({ eventId: document.id, ...document.data() })) });
        return;
    }
    response.status(405).json({ error: "Method not allowed" });
}
async function recordAnalyticsEvent(firebaseAdmin, userId, eventType, timestamp, metrics = {}) {
    const db = firebaseAdmin.firestore();
    const sanitizedMetrics = sanitizeMetrics(metrics);
    await db.collection("analytics").add({
        userId,
        eventType: (0, http_1.limitString)(eventType, "unknown", 80),
        timestamp,
        metrics: sanitizedMetrics,
    });
    await updateAnalyticsRollups(firebaseAdmin, userId, eventType, timestamp, sanitizedMetrics);
}
async function updateAnalyticsRollups(firebaseAdmin, userId, eventType, timestamp, metrics) {
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
        }, { merge: true }),
        db.collection("platformAnalytics").doc(day).set({
            windowId: day,
            windowStart: Date.parse(`${day}T00:00:00.000Z`),
            totalEvents: fieldValue.increment(1),
            totalLearningTimeSeconds: fieldValue.increment(learningTimeSeconds),
            totalLatencyMs: fieldValue.increment(latencyMs),
            [`eventCounts.${eventType}`]: fieldValue.increment(1),
            updatedAt: Date.now(),
        }, { merge: true }),
    ]);
}
function sanitizeMetrics(metrics) {
    return Object.fromEntries(Object.entries(metrics)
        .slice(0, 30)
        .map(([key, value]) => [key.slice(0, 60), normalizeMetricValue(value)]));
}
function normalizeMetricValue(value) {
    if (typeof value === "string")
        return value.slice(0, 500);
    if (typeof value === "number" || typeof value === "boolean")
        return value;
    return null;
}
