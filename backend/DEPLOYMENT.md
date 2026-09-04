# Deployment Preparation

1. Create a Firebase project.
2. Enable Authentication providers.
3. Enable Firestore in production mode.
4. Enable Firebase Storage.
5. Install Firebase CLI.
6. From `backend/functions`, run `npm install`.
7. From `backend`, run `firebase use PROJECT_ID`.
8. Run `firebase emulators:start` for local validation.
9. Deploy rules and indexes with `firebase deploy --only firestore,storage`.
10. Deploy Cloud Functions with `firebase deploy --only functions`.

## Monitoring

Enable:

- Firebase Crashlytics for mobile crash health.
- Firebase Performance Monitoring for startup, network, and screen traces.
- Cloud Functions logs and error reporting.
- Firestore usage dashboards and index query analysis.

## Scalability

- Keep mobile writes append-only for analytics.
- Use aggregate documents for dashboards.
- Limit Cloud Function request payloads.
- Use Storage signed URLs for private assets.
- Use Firestore indexes for hot query paths.
- Keep AI model rollout metadata separate from model binaries.
