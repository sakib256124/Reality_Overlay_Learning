"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.handleAdminRoute = handleAdminRoute;
exports.upsertEducationalObject = upsertEducationalObject;
exports.publishModelVersion = publishModelVersion;
exports.registerEducationalAsset = registerEducationalAsset;
const auth_middleware_1 = require("../authentication/auth-middleware");
const model_repository_1 = require("../ai-services/model-repository");
const storage_service_1 = require("../storage/storage-service");
const http_1 = require("../api-gateway/http");
async function handleAdminRoute(firebaseAdmin, request, response, auth) {
    (0, auth_middleware_1.requireAdmin)(auth);
    if (request.path.includes("/admin/objects")) {
        (0, http_1.requireMethod)(request, ["POST", "PUT"]);
        const payload = sanitizeAdminObjectPayload(request.body ?? {});
        await upsertEducationalObject(firebaseAdmin, auth, String(payload.objectId), payload);
        response.json({ status: "ok", objectId: payload.objectId });
        return;
    }
    if (request.path.includes("/admin/models")) {
        if (request.method === "GET") {
            const snapshot = await firebaseAdmin.firestore()
                .collection("aiModels")
                .orderBy("updatedAt", "desc")
                .limit(100)
                .get();
            response.json({ models: snapshot.docs.map((document) => ({ modelId: document.id, ...document.data() })) });
            return;
        }
        (0, http_1.requireMethod)(request, ["POST", "PUT"]);
        const descriptor = sanitizeModelDescriptor(request.body ?? {});
        await publishModelVersion(firebaseAdmin, auth, descriptor);
        response.json({ status: "ok", modelId: descriptor.modelId });
        return;
    }
    if (request.path.includes("/admin/storage-assets")) {
        (0, http_1.requireMethod)(request, ["POST", "PUT"]);
        const asset = sanitizeStorageAsset(request.body ?? {});
        await registerEducationalAsset(firebaseAdmin, auth, asset);
        response.json({ status: "ok", storagePath: asset.storagePath });
        return;
    }
    if (request.path.includes("/admin/analytics")) {
        (0, http_1.requireMethod)(request, ["GET"]);
        const snapshot = await firebaseAdmin.firestore()
            .collection("platformAnalytics")
            .orderBy("windowStart", "desc")
            .limit(30)
            .get();
        response.json({ windows: snapshot.docs.map((document) => ({ windowId: document.id, ...document.data() })) });
        return;
    }
    response.status(404).json({ error: "Admin route not found", code: "admin_route_not_found" });
}
async function upsertEducationalObject(firebaseAdmin, auth, objectId, payload) {
    (0, auth_middleware_1.requireAdmin)(auth);
    await firebaseAdmin.firestore().collection("objects").doc(objectId).set({
        ...payload,
        objectId,
        updatedAt: Date.now(),
    }, { merge: true });
}
async function publishModelVersion(firebaseAdmin, auth, descriptor) {
    (0, auth_middleware_1.requireAdmin)(auth);
    await (0, model_repository_1.registerModel)(firebaseAdmin, descriptor);
}
async function registerEducationalAsset(firebaseAdmin, auth, asset) {
    (0, auth_middleware_1.requireAdmin)(auth);
    await (0, storage_service_1.registerStorageAsset)(firebaseAdmin, asset);
}
function sanitizeAdminObjectPayload(payload) {
    const objectId = (0, http_1.sanitizeId)(payload.objectId ?? payload.name, "object", 100);
    const searchKeywords = new Set([
        objectId,
        ...(0, http_1.limitString)(payload.name, "", 120).toLowerCase().split(/\s+/),
        ...(0, http_1.limitString)(payload.category, "", 80).toLowerCase().split(/\s+/),
        ...(0, http_1.arrayOfStrings)(payload.tags, 30, 40).map((tag) => tag.toLowerCase()),
    ].filter(Boolean));
    return {
        objectId,
        name: (0, http_1.limitString)(payload.name, "", 120),
        category: (0, http_1.limitString)(payload.category, "", 80),
        scientificName: (0, http_1.limitString)(payload.scientificName, "", 160),
        description: (0, http_1.limitString)(payload.description, "", 4000),
        verified: Boolean(payload.verified ?? true),
        tags: (0, http_1.arrayOfStrings)(payload.tags, 30, 40),
        uses: (0, http_1.arrayOfStrings)(payload.uses, 30, 120),
        facts: (0, http_1.arrayOfStrings)(payload.facts, 30, 200),
        imageUrl: (0, http_1.limitString)(payload.imageUrl, "", 500),
        modelUrl: (0, http_1.limitString)(payload.modelUrl, "", 500),
        searchKeywords: Array.from(searchKeywords).slice(0, 80),
    };
}
function sanitizeModelDescriptor(payload) {
    const modelFamily = (0, http_1.sanitizeId)(payload.modelFamily, "object-detection", 80);
    const version = (0, http_1.limitString)(payload.version, "1.0", 40);
    return {
        modelId: (0, http_1.sanitizeId)(payload.modelId, `${modelFamily}-v${version}`, 120),
        modelFamily,
        version,
        status: payload.status === "staging" || payload.status === "deprecated" ? payload.status : "production",
        accuracy: Math.max(0, Math.min(1, Number(payload.accuracy ?? 0))),
        storagePath: (0, http_1.limitString)(payload.storagePath, `ai-models/${modelFamily}/${version}/model.tflite`, 500),
        minAppVersion: (0, http_1.limitString)(payload.minAppVersion, "1.0.0", 40),
        updatedAt: Date.now(),
    };
}
function sanitizeStorageAsset(payload) {
    const assetType = payload.assetType === "model3d" || payload.assetType === "aiModel" || payload.assetType === "resource"
        ? payload.assetType
        : "image";
    return {
        objectId: (0, http_1.sanitizeId)(payload.objectId, "global", 100),
        assetType,
        version: (0, http_1.limitString)(payload.version, "1", 40),
        storagePath: (0, http_1.limitString)(payload.storagePath, "", 500),
        cdnUrl: (0, http_1.limitString)(payload.cdnUrl, "", 500),
        checksum: (0, http_1.limitString)(payload.checksum, "", 160),
        updatedAt: Date.now(),
    };
}
