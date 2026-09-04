# ROLA Module 17 Cloud AI Infrastructure

## Complete Cloud Architecture Diagram

```text
Mobile Application
  - Jetpack Compose + MVVM
  - Room offline cache
  - Firebase Auth token provider
  - Retrofit cloud API client
  - Firebase Storage asset client
        |
        v
API Gateway
  - HTTPS Cloud Function: api
  - Firebase ID token verification
  - Role checks and payload limits
  - REST routes with GraphQL-ready service boundary
        |
        v
Backend Services
  - authentication
  - user-management
  - object-knowledge
  - ai-services
  - analytics
  - recommendation
  - storage
  - admin-management
        |
        v
Cloud Database and Storage
  - Firestore: users, objects, scanHistory, quizResults
  - Firestore: learningProfiles, translations, 3DModels
  - Firestore: aiModels, analytics, learningAnalytics, platformAnalytics
  - Storage: images, 3D models, TFLite files, educational resources
        |
        v
AI Services
  - TFLite model repository
  - Cloud recognition fallback adapter
  - AI tutor request adapter
  - Translation request adapter
  - Recommendation processing
  - Model performance telemetry
```

## Firebase Configuration

Collections:

```text
users
objects
scanHistory
users/{userId}/quizResults
learningProfiles
recommendations
translations/{languageCode}/translatedContent
3DModels
aiModels
analytics
learningAnalytics
platformAnalytics
aiInteractions
storageAssets
researchTasks
scientificSources
knowledgeUpdates
learningMaterials
contentVersions
institutions
roles
departments
courses
classes
members
assignments
submissions
classroomSessions
teacherAnalytics
studentReports
lmsConnections
auditLogs
curriculums
lessons
activities
assessments
teachingPlans
teacherReviews
lessonAnalytics
globalInstitutions
institutionConnections
verificationRecords
sharedKnowledgeResources
marketplaceListings
collaborationRooms
projectWorkspaces
sharedLearningSessions
communityPosts
discussionThreads
learningGroups
contributionScores
recognitionBadges
globalAnalytics
globalOpportunities
tenants/{institutionId}
```

Storage:

```text
objects/{objectId}/images/{version}/{fileName}
objects/{objectId}/models/{version}/{fileName}
ai-models/{modelFamily}/{version}/model.tflite
education-resources/{languageCode}/{fileName}
user-uploads/{userId}/{fileName}
```

Cloud Functions:

```text
api
  region: us-central1
  runtime: nodejs20
  memory: 512MiB
  maxInstances: 200
  timeoutSeconds: 60
```

Rules and indexes:

```text
backend/firestore.rules
backend/storage.rules
backend/firestore.indexes.json
backend/firebase.json
```

## Backend Folder Structure

```text
backend/
  authentication/
  user-management/
  object-knowledge/
  ai-services/
  analytics/
  recommendation/
  storage/
  api-gateway/
  admin-management/
  functions/
```

## API Design

Base path:

```text
https://us-central1-PROJECT_ID.cloudfunctions.net/api
```

Endpoints:

```text
GET    /users
PUT    /users
GET    /objects
POST   /objects
GET    /objects/{id}
GET    /search?q=
POST   /recognition
POST   /chat
POST   /translate
GET    /recommendations
POST   /recommendations
GET    /analytics
POST   /analytics
POST   /admin/objects
GET    /admin/models
POST   /admin/models
POST   /admin/storage-assets
GET    /admin/analytics
```

Endpoint responsibilities:

- `/users`: authenticated profile and preference management.
- `/objects`: public educational object reads and admin content writes.
- `/search`: indexed keyword search across object knowledge.
- `/recognition`: cloud-assisted model lookup and recognition fallback preparation.
- `/chat`: AI tutor request adapter with learning profile context.
- `/translate`: cloud translation adapter and translation cache metadata.
- `/recommendations`: user-specific recommendation reads and generation.
- `/analytics`: event ingestion and recent user event reads.
- `/admin/*`: educator/admin content, storage asset, AI model, and platform analytics management.
- `/curriculums`, `/lessons`, `/assessments`, `/teachingPlans`, `/teacherReviews`, and `/lessonAnalytics`: AI teacher planning, review, distribution, and effectiveness reporting.

GraphQL preparation: each route is backed by a bounded service module, so future GraphQL resolvers can call the same service functions without changing mobile features.

## AI Model Management

AI model metadata is stored in `aiModels`; binaries live in Firebase Storage:

```text
aiModels/{modelId}
  modelFamily: object-detection
  version: 2.0
  status: production
  accuracy: 0.96
  storagePath: ai-models/object-detection/2.0/model.tflite
  minAppVersion: 1.0.0
  updatedAt: timestamp
```

Admin model publishing flows through `POST /admin/models`. Clients request the latest production model through cloud AI routes while existing on-device recognition remains primary.

## Analytics Pipeline

Collected events:

- Objects scanned.
- Learning time.
- Quiz performance.
- Search behavior.
- AI tutor, translation, and recognition interactions.
- API latency and AI response time.

Generated outputs:

- Raw events in `analytics`.
- Per-user rollups in `learningAnalytics`.
- Daily platform windows in `platformAnalytics`.
- AI request metadata in `aiInteractions`.

## Offline and Cloud Synchronization

```text
Local Room Database
        |
        v
Sync Manager
        |
        v
Cloud API / Firestore
        |
        v
Updated object, profile, history, quiz, recommendation, and analytics data
        |
        v
Local cache refresh
```

The mobile app remains Room-first. Cloud writes are user-scoped, append-only where possible, and safe to retry.

## Database Optimization

- Use composite indexes for user history, object categories, recommendations, and analytics event streams.
- Keep analytics append-only and generate aggregate documents in `learningAnalytics`.
- Use Storage for large files; Firestore stores metadata and URLs only.
- Keep AI model files versioned and roll out through `aiModels` metadata.
- Use local Room cache as the first read path; sync writes opportunistically.
- Use `searchKeywords` arrays for scalable object search instead of collection scans.
- Use daily `platformAnalytics/{yyyy-mm-dd}` documents for dashboard reads.
- Keep user-owned high-write collections partitioned by `userId` fields and indexed timestamps.
- Store large educational media and model binaries outside Firestore.
- Cache stable object knowledge and model descriptors on-device.

## Scalability Strategy

- Cloud Functions provide serverless horizontal scale up to configured `maxInstances`.
- API payloads are bounded to reduce memory and abuse risk.
- Firestore indexes cover hot query paths before production rollout.
- Analytics are write-optimized and summarized asynchronously through rollup documents.
- Firebase Storage and CDN delivery handle images, 3D models, resources, and TFLite binaries.
- AI adapters isolate high-latency model/provider calls from mobile UI architecture.

## Security Checklist

- Require Firebase ID tokens for protected API calls.
- Use Firebase custom claims for `educator` and `admin`.
- Keep object reads public, object writes admin-only.
- Keep user profile, history, recommendations, and analytics user-scoped.
- Keep model uploads and Storage writes admin-only.
- Limit payload lengths in API gateway services.
- Use HTTPS Cloud Functions only.
- Store secrets in Cloud Functions environment/secrets, not the app.
- Validate Storage file sizes by asset class.
- Keep translation and AI interaction metadata privacy-preserving.
- Use Firestore rules for direct mobile access and Admin SDK checks in Cloud Functions.
- Review service account permissions before deployment.

## Monitoring

- Firebase Analytics: learning funnel, retention, feature usage.
- Crashlytics: app crash health.
- Firebase Performance Monitoring: mobile network traces and screen performance.
- Cloud Functions logs: API latency, errors, cold starts.
- Firestore dashboards: document reads/writes, index pressure, hot documents.
- Storage metrics: bandwidth, object count, large file delivery.
- Custom analytics: `latencyMs`, `eventCounts`, AI interaction volumes.

## Testing Procedure

- Run `npm run build` in `backend/functions` for TypeScript validation.
- Run Firebase emulators from `backend` and exercise API routes with signed test tokens.
- Validate Firestore and Storage rules with Firebase emulator security tests.
- Seed thousands of `objects`, `analytics`, and `recommendations` documents to verify indexes.
- Simulate concurrent `/analytics`, `/search`, `/chat`, and `/recommendations` traffic.
- Verify offline sync by disabling network, writing Room data, reconnecting, and checking cloud convergence.
- Test admin-only routes with learner, educator, admin, and unauthenticated token scenarios.

## Deployment Steps

1. Create Firebase project.
2. Add Android app and `google-services.json`.
3. Enable Auth, Firestore, Storage, Functions.
4. Install Firebase CLI.
5. Run `npm install` in `backend/functions`.
6. Run Firebase emulators from `backend`.
7. Deploy rules and indexes.
8. Deploy functions.
9. Seed `objects` and `aiModels`.
10. Configure monitoring dashboards.
