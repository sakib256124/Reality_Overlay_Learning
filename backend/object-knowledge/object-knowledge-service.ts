import {AuthContext, requireAdmin} from "../authentication/auth-middleware";
import {arrayOfStrings, limitString, sanitizeId} from "../api-gateway/http";

type Request = any;
type Response = any;
type FirebaseAdmin = any;

export async function handleObjectRoute(
  firebaseAdmin: FirebaseAdmin,
  request: Request,
  response: Response,
  auth: AuthContext,
): Promise<void> {
  const db = firebaseAdmin.firestore();

  if (request.method === "GET" && request.path.includes("/search")) {
    const query = limitString(request.query.q, "", 120).toLowerCase();
    const tokens = query.split(/\s+/).filter(Boolean).slice(0, 5);
    const collection = db.collection("objects");
    const snapshot = tokens.length > 0
      ? await collection.where("searchKeywords", "array-contains-any", tokens).limit(50).get()
      : await collection.orderBy("updatedAt", "desc").limit(50).get();
    const objects = snapshot.docs.map((document: any) => ({objectId: document.id, ...document.data()}));
    response.json(objects);
    return;
  }

  if (request.method === "GET") {
    const objectId = request.path.split("/").filter(Boolean).pop();
    if (objectId && objectId !== "objects") {
      const snapshot = await db.collection("objects").doc(objectId).get();
      response.json({objectId, ...(snapshot.data() ?? {})});
      return;
    }

    const category = limitString(request.query.category, "", 80);
    const limit = Math.max(1, Math.min(100, Number(request.query.limit ?? 100)));
    const query = category
      ? db.collection("objects").where("category", "==", category).orderBy("updatedAt", "desc").limit(limit)
      : db.collection("objects").orderBy("updatedAt", "desc").limit(limit);
    const snapshot = await query.get();
    response.json(snapshot.docs.map((document: any) => ({objectId: document.id, ...document.data()})));
    return;
  }

  if (request.method === "POST" || request.method === "PUT") {
    requireAdmin(auth);
    const payload = sanitizeObjectPayload(request.body ?? {});
    const objectId = String(payload.objectId);
    await db.collection("objects").doc(objectId).set({...payload, updatedAt: Date.now()}, {merge: true});
    response.json({status: "ok", objectId});
    return;
  }

  response.status(405).json({error: "Method not allowed"});
}

function sanitizeObjectPayload(payload: Record<string, unknown>): Record<string, unknown> {
  const objectId = sanitizeId(payload.objectId ?? payload.name, "object", 80);
  const name = limitString(payload.name, "", 120);
  const category = limitString(payload.category, "", 80);
  const searchKeywords = new Set([
    objectId,
    ...name.toLowerCase().split(/\s+/),
    ...category.toLowerCase().split(/\s+/),
    ...arrayOfStrings(payload.tags, 30, 40).map((tag) => tag.toLowerCase()),
  ].filter(Boolean));

  return {
    objectId,
    name,
    category,
    scientificName: limitString(payload.scientificName, "", 160),
    description: limitString(payload.description, "", 4000),
    uses: arrayOfStrings(payload.uses, 30, 120),
    facts: arrayOfStrings(payload.facts, 30, 200),
    tags: arrayOfStrings(payload.tags, 30, 40),
    imageUrl: limitString(payload.imageUrl, "", 500),
    modelUrl: limitString(payload.modelUrl, "", 500),
    searchKeywords: Array.from(searchKeywords).slice(0, 80),
    verified: Boolean(payload.verified ?? false),
  };
}
