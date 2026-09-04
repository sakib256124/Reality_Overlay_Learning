import {requireAdmin, type AuthContext} from "../authentication/auth-middleware";
import {registerModel, type AIModelDescriptor} from "../ai-services/model-repository";
import {registerStorageAsset, type StorageAsset} from "../storage/storage-service";
import {arrayOfStrings, limitString, requireMethod, sanitizeId} from "../api-gateway/http";

type Request = any;
type Response = any;
type FirebaseAdmin = any;

export async function handleAdminRoute(
  firebaseAdmin: FirebaseAdmin,
  request: Request,
  response: Response,
  auth: AuthContext,
): Promise<void> {
  requireAdmin(auth);

  if (request.path.includes("/admin/objects")) {
    requireMethod(request, ["POST", "PUT"]);
    const payload = sanitizeAdminObjectPayload(request.body ?? {});
    await upsertEducationalObject(firebaseAdmin, auth, String(payload.objectId), payload);
    response.json({status: "ok", objectId: payload.objectId});
    return;
  }

  if (request.path.includes("/admin/models")) {
    if (request.method === "GET") {
      const snapshot = await firebaseAdmin.firestore()
        .collection("aiModels")
        .orderBy("updatedAt", "desc")
        .limit(100)
        .get();
      response.json({models: snapshot.docs.map((document: any) => ({modelId: document.id, ...document.data()}))});
      return;
    }

    requireMethod(request, ["POST", "PUT"]);
    const descriptor = sanitizeModelDescriptor(request.body ?? {});
    await publishModelVersion(firebaseAdmin, auth, descriptor);
    response.json({status: "ok", modelId: descriptor.modelId});
    return;
  }

  if (request.path.includes("/admin/storage-assets")) {
    requireMethod(request, ["POST", "PUT"]);
    const asset = sanitizeStorageAsset(request.body ?? {});
    await registerEducationalAsset(firebaseAdmin, auth, asset);
    response.json({status: "ok", storagePath: asset.storagePath});
    return;
  }

  if (request.path.includes("/admin/analytics")) {
    requireMethod(request, ["GET"]);
    const snapshot = await firebaseAdmin.firestore()
      .collection("platformAnalytics")
      .orderBy("windowStart", "desc")
      .limit(30)
      .get();
    response.json({windows: snapshot.docs.map((document: any) => ({windowId: document.id, ...document.data()}))});
    return;
  }

  response.status(404).json({error: "Admin route not found", code: "admin_route_not_found"});
}

export async function upsertEducationalObject(
  firebaseAdmin: FirebaseAdmin,
  auth: AuthContext,
  objectId: string,
  payload: Record<string, unknown>,
): Promise<void> {
  requireAdmin(auth);
  await firebaseAdmin.firestore().collection("objects").doc(objectId).set(
    {
      ...payload,
      objectId,
      updatedAt: Date.now(),
    },
    {merge: true},
  );
}

export async function publishModelVersion(
  firebaseAdmin: FirebaseAdmin,
  auth: AuthContext,
  descriptor: AIModelDescriptor,
): Promise<void> {
  requireAdmin(auth);
  await registerModel(firebaseAdmin, descriptor);
}

export async function registerEducationalAsset(
  firebaseAdmin: FirebaseAdmin,
  auth: AuthContext,
  asset: StorageAsset,
): Promise<void> {
  requireAdmin(auth);
  await registerStorageAsset(firebaseAdmin, asset);
}

function sanitizeAdminObjectPayload(payload: Record<string, unknown>): Record<string, unknown> {
  const objectId = sanitizeId(payload.objectId ?? payload.name, "object", 100);
  const searchKeywords = new Set([
    objectId,
    ...limitString(payload.name, "", 120).toLowerCase().split(/\s+/),
    ...limitString(payload.category, "", 80).toLowerCase().split(/\s+/),
    ...arrayOfStrings(payload.tags, 30, 40).map((tag) => tag.toLowerCase()),
  ].filter(Boolean));

  return {
    objectId,
    name: limitString(payload.name, "", 120),
    category: limitString(payload.category, "", 80),
    scientificName: limitString(payload.scientificName, "", 160),
    description: limitString(payload.description, "", 4000),
    verified: Boolean(payload.verified ?? true),
    tags: arrayOfStrings(payload.tags, 30, 40),
    uses: arrayOfStrings(payload.uses, 30, 120),
    facts: arrayOfStrings(payload.facts, 30, 200),
    imageUrl: limitString(payload.imageUrl, "", 500),
    modelUrl: limitString(payload.modelUrl, "", 500),
    searchKeywords: Array.from(searchKeywords).slice(0, 80),
  };
}

function sanitizeModelDescriptor(payload: Record<string, unknown>): AIModelDescriptor {
  const modelFamily = sanitizeId(payload.modelFamily, "object-detection", 80);
  const version = limitString(payload.version, "1.0", 40);
  return {
    modelId: sanitizeId(payload.modelId, `${modelFamily}-v${version}`, 120),
    modelFamily,
    version,
    status: payload.status === "staging" || payload.status === "deprecated" ? payload.status : "production",
    accuracy: Math.max(0, Math.min(1, Number(payload.accuracy ?? 0))),
    storagePath: limitString(payload.storagePath, `ai-models/${modelFamily}/${version}/model.tflite`, 500),
    minAppVersion: limitString(payload.minAppVersion, "1.0.0", 40),
    updatedAt: Date.now(),
  };
}

function sanitizeStorageAsset(payload: Record<string, unknown>): StorageAsset {
  const assetType = payload.assetType === "model3d" || payload.assetType === "aiModel" || payload.assetType === "resource"
    ? payload.assetType
    : "image";
  return {
    objectId: sanitizeId(payload.objectId, "global", 100),
    assetType,
    version: limitString(payload.version, "1", 40),
    storagePath: limitString(payload.storagePath, "", 500),
    cdnUrl: limitString(payload.cdnUrl, "", 500),
    checksum: limitString(payload.checksum, "", 160),
    updatedAt: Date.now(),
  };
}
