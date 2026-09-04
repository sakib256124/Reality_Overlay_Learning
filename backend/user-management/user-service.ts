import {AuthContext, requireUser} from "../authentication/auth-middleware";

type Request = any;
type Response = any;
type FirebaseAdmin = any;

export async function handleUserRoute(
  firebaseAdmin: FirebaseAdmin,
  request: Request,
  response: Response,
  auth: AuthContext,
): Promise<void> {
  const uid = requireUser(auth);
  const db = firebaseAdmin.firestore();

  if (request.method === "GET") {
    const snapshot = await db.collection("users").doc(uid).get();
    response.json({userId: uid, ...(snapshot.data() ?? {})});
    return;
  }

  if (request.method === "PUT" || request.method === "POST") {
    const payload = sanitizeUserPayload(request.body ?? {});
    await db.collection("users").doc(uid).set(
      {
        ...payload,
        userId: uid,
        updatedAt: Date.now(),
      },
      {merge: true},
    );
    response.json({status: "ok"});
    return;
  }

  response.status(405).json({error: "Method not allowed"});
}

function sanitizeUserPayload(payload: Record<string, unknown>): Record<string, unknown> {
  return {
    name: String(payload.name ?? "").slice(0, 80),
    email: String(payload.email ?? "").slice(0, 120),
    preferredLanguage: String(payload.preferredLanguage ?? "en").slice(0, 12),
    personalizationEnabled: Boolean(payload.personalizationEnabled ?? true),
  };
}
