"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.handleUserRoute = handleUserRoute;
const auth_middleware_1 = require("../authentication/auth-middleware");
async function handleUserRoute(firebaseAdmin, request, response, auth) {
    const uid = (0, auth_middleware_1.requireUser)(auth);
    const db = firebaseAdmin.firestore();
    if (request.method === "GET") {
        const snapshot = await db.collection("users").doc(uid).get();
        response.json({ userId: uid, ...(snapshot.data() ?? {}) });
        return;
    }
    if (request.method === "PUT" || request.method === "POST") {
        const payload = sanitizeUserPayload(request.body ?? {});
        await db.collection("users").doc(uid).set({
            ...payload,
            userId: uid,
            updatedAt: Date.now(),
        }, { merge: true });
        response.json({ status: "ok" });
        return;
    }
    response.status(405).json({ error: "Method not allowed" });
}
function sanitizeUserPayload(payload) {
    return {
        name: String(payload.name ?? "").slice(0, 80),
        email: String(payload.email ?? "").slice(0, 120),
        preferredLanguage: String(payload.preferredLanguage ?? "en").slice(0, 12),
        personalizationEnabled: Boolean(payload.personalizationEnabled ?? true),
    };
}
