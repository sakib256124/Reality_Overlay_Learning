"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.handleAiRoute = handleAiRoute;
const auth_middleware_1 = require("../authentication/auth-middleware");
const analytics_service_1 = require("../analytics/analytics-service");
const model_repository_1 = require("./model-repository");
async function handleAiRoute(firebaseAdmin, request, response, auth) {
    const uid = (0, auth_middleware_1.requireUser)(auth);
    const db = firebaseAdmin.firestore();
    const startedAt = Date.now();
    if (request.method !== "POST") {
        response.status(405).json({ error: "Method not allowed" });
        return;
    }
    if (request.path.includes("/recognition")) {
        const model = await (0, model_repository_1.getProductionModel)(firebaseAdmin, "object-detection");
        await (0, analytics_service_1.recordAnalyticsEvent)(firebaseAdmin, uid, "recognition_request", startedAt, {
            modelId: model.modelId,
            latencyMs: Date.now() - startedAt,
        });
        response.json({
            model,
            detections: [],
            message: "Upload image feature is prepared; mobile on-device recognition remains primary.",
            latencyMs: Date.now() - startedAt,
        });
        return;
    }
    if (request.path.includes("/chat")) {
        const prompt = String(request.body?.message ?? "").slice(0, 2000);
        const profile = await db.collection("learningProfiles").doc(uid).get();
        const tutorResponse = buildTutorResponse(prompt, profile.data());
        const latencyMs = Date.now() - startedAt;
        await db.collection("aiInteractions").add({
            userId: uid,
            interactionType: "chat",
            promptLength: prompt.length,
            responseLength: tutorResponse.length,
            latencyMs,
            timestamp: Date.now(),
        });
        await (0, analytics_service_1.recordAnalyticsEvent)(firebaseAdmin, uid, "chat_request", startedAt, { hasProfile: profile.exists, latencyMs });
        response.json({
            response: tutorResponse,
            latencyMs,
        });
        return;
    }
    if (request.path.includes("/translate")) {
        const text = String(request.body?.text ?? "").slice(0, 5000);
        const targetLanguage = String(request.body?.targetLanguage ?? "en").slice(0, 12);
        const latencyMs = Date.now() - startedAt;
        const translationId = `${uid}_${targetLanguage}_${startedAt}`;
        await db.collection("translations").doc(targetLanguage).collection("translatedContent").doc(translationId).set({
            userId: uid,
            sourceTextHash: simpleHash(text),
            targetLanguage,
            provider: "prepared-cloud-translation-adapter",
            createdAt: Date.now(),
        }, { merge: true });
        await (0, analytics_service_1.recordAnalyticsEvent)(firebaseAdmin, uid, "translation_request", startedAt, { targetLanguage, latencyMs });
        response.json({
            translatedText: text,
            targetLanguage,
            provider: "prepared-cloud-translation-adapter",
            latencyMs,
        });
        return;
    }
    response.status(404).json({ error: "AI route not found" });
}
function buildTutorResponse(prompt, profile) {
    const level = String(profile?.learningLevel ?? "Beginner");
    if (!prompt) {
        return `Ask a question and I will adapt the explanation for a ${level} learner.`;
    }
    return `For your ${level} level: ${prompt}`;
}
function simpleHash(value) {
    let hash = 0;
    for (let index = 0; index < value.length; index += 1) {
        hash = ((hash << 5) - hash + value.charCodeAt(index)) | 0;
    }
    return Math.abs(hash).toString(16);
}
