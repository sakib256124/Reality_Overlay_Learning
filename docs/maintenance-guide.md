# ROLA Maintenance Guide

## Monitoring Dashboard

Track:

- System health: Cloud Functions errors, Firestore quota, Storage bandwidth.
- User activity: active learners, sessions, scans, quizzes, tutor messages.
- AI performance: TFLite inference time, cloud AI latency, model accuracy metadata.
- Crash reports: crash-free users, ANRs, affected releases, obfuscated mapping coverage.
- Cloud usage: reads, writes, indexes, function invocations, storage downloads.

## Backup And Recovery

- Enable scheduled Firestore exports to a locked Google Cloud Storage bucket.
- Export Storage metadata and keep model binaries versioned by path.
- Keep `aiModels` production and rollback descriptors.
- Keep `contentVersions` for educational content rollback.
- Restore order: Auth users, Firestore data, Storage assets, Cloud Functions, app rollout.

## Update Strategy

- Feature updates go through dev, staging, internal testing, then production rollout.
- Security patches can skip open testing after staging validation and internal smoke tests.
- AI model updates are published as new `aiModels` documents with production and rollback metadata.
- Database migrations must be backward compatible and tested from the previous production version.
- Cloud APIs should preserve request/response compatibility for at least one mobile release cycle.

## Release Health Gates

- Crash-free users above 99.5%.
- ANR rate below Play quality thresholds.
- Startup latency and AR session latency within staging baseline.
- AI inference latency within model-specific SLO.
- No high-severity security rule findings.
- No migration failures in internal testing.
