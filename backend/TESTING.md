# Module 17 Backend Testing Procedure

## Local Validation

```bash
cd backend/functions
npm run build
cd ..
firebase emulators:start --only auth,firestore,storage,functions
```

## API Performance

- Generate Firebase emulator users for learner, educator, and admin roles.
- Send concurrent traffic to `/analytics`, `/search`, `/chat`, `/translate`, and `/recommendations`.
- Track p50, p95, and p99 latency from Cloud Functions logs.
- Confirm payload limits reject oversized prompts, translations, object bodies, and metrics.

## Database Scalability

- Seed at least 100,000 `objects` documents with `searchKeywords`.
- Seed high-volume `analytics` events by `userId` and `timestamp`.
- Verify `firestore.indexes.json` satisfies object category, recommendation, analytics, and AI model queries.
- Confirm dashboard reads use `learningAnalytics` and `platformAnalytics` rollups instead of scanning raw events.

## Security Rules

- Learner can read and update only their own profile.
- Learner can create only their own analytics events.
- Learner cannot write `objects`, `aiModels`, `storageAssets`, or admin analytics.
- Educator/admin can update verified educational content and model metadata.
- Storage uploads enforce role and file size limits.

## Cloud Synchronization

- Create local Room scan/history/quiz data while offline.
- Reconnect and run sync.
- Verify Firestore contains the expected user-scoped writes.
- Re-run sync to confirm idempotent behavior and no duplicate recommendations.

## Large User Simulation

- Use the Firebase emulator suite for development-scale load.
- For staging, use a separate Firebase project with seeded indexes and synthetic users.
- Ramp concurrent users gradually and watch Cloud Functions max instances, Firestore write rates, and Storage bandwidth.
