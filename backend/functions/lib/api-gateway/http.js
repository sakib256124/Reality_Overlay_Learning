"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.HttpError = void 0;
exports.sendError = sendError;
exports.requireMethod = requireMethod;
exports.limitString = limitString;
exports.sanitizeId = sanitizeId;
exports.arrayOfStrings = arrayOfStrings;
class HttpError extends Error {
    statusCode;
    code;
    constructor(statusCode, message, code = "backend_error") {
        super(message);
        this.statusCode = statusCode;
        this.code = code;
    }
}
exports.HttpError = HttpError;
function sendError(response, error) {
    if (error instanceof HttpError) {
        response.status(error.statusCode).json({ error: error.message, code: error.code });
        return;
    }
    const message = error instanceof Error ? error.message : "Unknown backend error";
    response.status(500).json({ error: message, code: "internal_error" });
}
function requireMethod(request, allowed) {
    if (!allowed.includes(request.method)) {
        throw new HttpError(405, "Method not allowed", "method_not_allowed");
    }
}
function limitString(value, fallback, maxLength) {
    return String(value ?? fallback).trim().slice(0, maxLength);
}
function sanitizeId(value, fallback, maxLength = 100) {
    const id = String(value ?? fallback)
        .toLowerCase()
        .replace(/[^a-z0-9_-]+/g, "_")
        .replace(/^_+|_+$/g, "")
        .slice(0, maxLength);
    return id || fallback;
}
function arrayOfStrings(value, maxItems, maxLength) {
    if (!Array.isArray(value))
        return [];
    return value.slice(0, maxItems).map((item) => String(item).trim().slice(0, maxLength)).filter(Boolean);
}
