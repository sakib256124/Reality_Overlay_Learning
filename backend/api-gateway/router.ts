import {verifyFirebaseAuth} from "../authentication/auth-middleware";
import {handleAdminRoute} from "../admin-management/admin-service";
import {handleAnalyticsRoute} from "../analytics/analytics-service";
import {handleAiRoute} from "../ai-services/ai-service";
import {handleAiTeacherRoute} from "../ai-teacher/ai-teacher-service";
import {sendError} from "./http";
import {handleObjectRoute} from "../object-knowledge/object-knowledge-service";
import {handleRecommendationRoute} from "../recommendation/recommendation-service";
import {handleUserRoute} from "../user-management/user-service";

type Request = any;
type Response = any;
type FirebaseAdmin = any;

export async function routeApiRequest(
  firebaseAdmin: FirebaseAdmin,
  request: Request,
  response: Response,
): Promise<void> {
  response.setHeader("Cache-Control", "no-store");
  response.setHeader("X-Content-Type-Options", "nosniff");
  response.setHeader("X-Frame-Options", "DENY");
  response.setHeader("Referrer-Policy", "no-referrer");

  if (request.method === "OPTIONS") {
    response.status(204).send("");
    return;
  }

  try {
    const auth = await verifyFirebaseAuth(firebaseAdmin, request);
    const path = request.path.replace(/^\/api/, "");

    if (path.startsWith("/users")) {
      await handleUserRoute(firebaseAdmin, request, response, auth);
      return;
    }
    if (path.startsWith("/objects") || path.startsWith("/search")) {
      await handleObjectRoute(firebaseAdmin, request, response, auth);
      return;
    }
    if (path.startsWith("/recognition") || path.startsWith("/chat") || path.startsWith("/translate")) {
      await handleAiRoute(firebaseAdmin, request, response, auth);
      return;
    }
    if (path.startsWith("/recommendations")) {
      await handleRecommendationRoute(firebaseAdmin, request, response, auth);
      return;
    }
    if (path.startsWith("/analytics")) {
      await handleAnalyticsRoute(firebaseAdmin, request, response, auth);
      return;
    }
    if (
      path.startsWith("/curriculums") ||
      path.startsWith("/lessons") ||
      path.startsWith("/assessments") ||
      path.startsWith("/teaching-plans") ||
      path.startsWith("/teacher-reviews") ||
      path.startsWith("/lesson-analytics")
    ) {
      await handleAiTeacherRoute(firebaseAdmin, request, response, auth);
      return;
    }
    if (path.startsWith("/admin")) {
      await handleAdminRoute(firebaseAdmin, request, response, auth);
      return;
    }

    response.status(404).json({error: "Route not found", code: "route_not_found"});
  } catch (error) {
    sendError(response, error);
  }
}
