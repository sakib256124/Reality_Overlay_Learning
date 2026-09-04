import {HttpError} from "../api-gateway/http";

type Request = any;
type FirebaseAdmin = any;

export interface AuthContext {
  uid: string | null;
  role: "learner" | "educator" | "admin";
}

export async function verifyFirebaseAuth(
  firebaseAdmin: FirebaseAdmin,
  request: Request,
): Promise<AuthContext> {
  const header = request.header("authorization") ?? "";
  const token = header.startsWith("Bearer ") ? header.substring("Bearer ".length) : null;
  if (!token) {
    return {uid: null, role: "learner"};
  }

  let decoded: any;
  try {
    decoded = await firebaseAdmin.auth().verifyIdToken(token);
  } catch (_) {
    throw new HttpError(401, "Invalid or expired authentication token", "invalid_token");
  }
  const role = decoded.role === "admin" || decoded.role === "educator" ? decoded.role : "learner";
  return {uid: decoded.uid, role};
}

export function requireUser(auth: AuthContext): string {
  if (!auth.uid) {
    throw new HttpError(401, "Authentication required", "authentication_required");
  }
  return auth.uid;
}

export function requireAdmin(auth: AuthContext): void {
  if (auth.role !== "admin" && auth.role !== "educator") {
    throw new HttpError(403, "Admin or educator role required", "insufficient_role");
  }
}
