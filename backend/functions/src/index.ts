import * as admin from "firebase-admin";
import {onRequest} from "firebase-functions/v2/https";
import {routeApiRequest} from "../../api-gateway/router";

admin.initializeApp();

export const api = onRequest(
  {
    region: "us-central1",
    cors: true,
    maxInstances: 200,
    timeoutSeconds: 60,
    memory: "512MiB",
  },
  async (request, response) => {
    await routeApiRequest(admin, request, response);
  },
);
