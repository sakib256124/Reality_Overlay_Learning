"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.handleRecommendationRoute = handleRecommendationRoute;
const auth_middleware_1 = require("../authentication/auth-middleware");
async function handleRecommendationRoute(firebaseAdmin, request, response, auth) {
    const uid = (0, auth_middleware_1.requireUser)(auth);
    const db = firebaseAdmin.firestore();
    if (request.method === "GET") {
        const snapshot = await db.collection("recommendations")
            .where("userId", "==", uid)
            .where("completed", "==", false)
            .orderBy("createdAt", "desc")
            .limit(20)
            .get();
        response.json({ recommendations: snapshot.docs.map((document) => ({ recommendationId: document.id, ...document.data() })) });
        return;
    }
    if (request.method === "POST") {
        const profile = (await db.collection("learningProfiles").doc(uid).get()).data();
        const recommendation = buildRecommendation(uid, profile);
        await db.collection("recommendations").doc(recommendation.recommendationId).set(recommendation, { merge: true });
        response.json({ recommendations: [recommendation] });
        return;
    }
    response.status(405).json({ error: "Method not allowed" });
}
function buildRecommendation(userId, profile) {
    const weakArea = Array.isArray(profile?.weakAreas) ? profile?.weakAreas[0] : null;
    const topic = String(weakArea ?? "Scientific classification");
    const id = `${userId}_${topic.toLowerCase().replace(/[^a-z0-9]+/g, "_")}`;
    return {
        recommendationId: id,
        userId,
        title: weakArea ? `Review ${topic}` : "Explore scientific classification",
        description: weakArea ? `Practice ${topic} before moving to harder concepts.` : "Build a stronger foundation by comparing object categories.",
        topic,
        type: weakArea ? "ReviewWeakArea" : "ExploreTopic",
        priority: weakArea ? "High" : "Medium",
        targetSkillLevel: String(profile?.learningLevel ?? "Beginner"),
        completed: false,
        createdAt: Date.now(),
        updatedAt: Date.now(),
    };
}
