"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.routeApiRequest = routeApiRequest;
const auth_middleware_1 = require("../authentication/auth-middleware");
const admin_service_1 = require("../admin-management/admin-service");
const analytics_service_1 = require("../analytics/analytics-service");
const ai_service_1 = require("../ai-services/ai-service");
const http_1 = require("./http");
const object_knowledge_service_1 = require("../object-knowledge/object-knowledge-service");
const recommendation_service_1 = require("../recommendation/recommendation-service");
const user_service_1 = require("../user-management/user-service");
async function routeApiRequest(firebaseAdmin, request, response) {
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("X-Content-Type-Options", "nosniff");
    response.setHeader("X-Frame-Options", "DENY");
    response.setHeader("Referrer-Policy", "no-referrer");
    if (request.method === "OPTIONS") {
        response.status(204).send("");
        return;
    }
    try {
        const auth = await (0, auth_middleware_1.verifyFirebaseAuth)(firebaseAdmin, request);
        const path = request.path.replace(/^\/api/, "");
        if (path.startsWith("/users")) {
            await (0, user_service_1.handleUserRoute)(firebaseAdmin, request, response, auth);
            return;
        }
        if (path.startsWith("/objects") || path.startsWith("/search")) {
            await (0, object_knowledge_service_1.handleObjectRoute)(firebaseAdmin, request, response, auth);
            return;
        }
        if (path.startsWith("/recognition") || path.startsWith("/chat") || path.startsWith("/translate")) {
            await (0, ai_service_1.handleAiRoute)(firebaseAdmin, request, response, auth);
            return;
        }
        if (path.startsWith("/recommendations")) {
            await (0, recommendation_service_1.handleRecommendationRoute)(firebaseAdmin, request, response, auth);
            return;
        }
        if (path.startsWith("/analytics")) {
            await (0, analytics_service_1.handleAnalyticsRoute)(firebaseAdmin, request, response, auth);
            return;
        }
        if (path.startsWith("/admin")) {
            await (0, admin_service_1.handleAdminRoute)(firebaseAdmin, request, response, auth);
            return;
        }
        response.status(404).json({ error: "Route not found", code: "route_not_found" });
    }
    catch (error) {
        (0, http_1.sendError)(response, error);
    }
}
