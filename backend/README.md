# ROLA Cloud AI Infrastructure

ROLA backend is organized as serverless service modules behind an API gateway. The Android app can keep using local Room-first workflows while cloud services provide scalable content, AI assistance, analytics, recommendations, and asset delivery.

## Architecture

```text
Android App
  -> API Gateway HTTPS Cloud Function
  -> Backend Service Router
  -> Firebase Auth + Firestore + Storage
  -> AI Services + Recommendation Jobs + Analytics
```

## Service Folders

- `authentication/`: auth middleware and role checks.
- `user-management/`: user profiles and preferences.
- `object-knowledge/`: educational object records, scientific data, images, 3D model metadata.
- `ai-services/`: recognition support, chatbot, translation, AI model registry.
- `analytics/`: learning event ingestion, usage metrics, performance telemetry.
- `recommendation/`: profile-aware next-step recommendations.
- `storage/`: Firebase Storage layout, asset versioning, secure access rules.
- `api-gateway/`: REST endpoint routing and request validation.
- `admin-management/`: educator/admin content publishing, model registry, asset metadata, analytics dashboards.

## Deployment Targets

- Firebase Authentication
- Cloud Firestore
- Firebase Storage
- Cloud Functions for Firebase
- Firebase Performance Monitoring and Crashlytics on the mobile side

## Production Capabilities

- Firebase ID token authentication and custom-claim role checks.
- Indexed object search and category listing.
- TFLite model registry with production/staging/deprecated states.
- Versioned Storage paths for object images, 3D models, AI models, and educational resources.
- Raw analytics ingestion with per-user and platform rollups.
- Admin API surface for educational content, storage assets, AI model versions, and platform analytics.
