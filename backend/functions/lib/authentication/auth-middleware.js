"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.verifyFirebaseAuth = verifyFirebaseAuth;
exports.requireUser = requireUser;
exports.requireAdmin = requireAdmin;
const http_1 = require("../api-gateway/http");
async function verifyFirebaseAuth(firebaseAdmin, request) {
    const header = request.header("authorization") ?? "";
    const token = header.startsWith("Bearer ") ? header.substring("Bearer ".length) : null;
    if (!token) {
        return { uid: null, role: "learner" };
    }
    let decoded;
    try {
        decoded = await firebaseAdmin.auth().verifyIdToken(token);
    }
    catch (_) {
        throw new http_1.HttpError(401, "Invalid or expired authentication token", "invalid_token");
    }
    const role = decoded.role === "admin" || decoded.role === "educator" ? decoded.role : "learner";
    return { uid: decoded.uid, role };
}
function requireUser(auth) {
    if (!auth.uid) {
        throw new http_1.HttpError(401, "Authentication required", "authentication_required");
    }
    return auth.uid;
}
function requireAdmin(auth) {
    if (auth.role !== "admin" && auth.role !== "educator") {
        throw new http_1.HttpError(403, "Admin or educator role required", "insufficient_role");
    }
}
