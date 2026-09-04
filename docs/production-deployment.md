# ROLA Module 21 Production Deployment

## Complete CI/CD Architecture Diagram

```text
Code Commit / Pull Request
        |
        v
GitHub Actions
  - build.yml
  - test.yml
  - release.yml
        |
        v
Build
  - Android SDK setup
  - Gradle cache
  - dev/staging/production flavors
  - secure Firebase config injection
        |
        v
Automated Tests
  - unit tests
  - lint
  - backend TypeScript build
  - emulator/security tests prepared
        |
        v
Security Checks
  - no signing secrets in repo
  - Firebase rules review
  - dependency/version review
  - R8 release config
        |
        v
Release Build
  - signed AAB
  - mapping file upload through Crashlytics plugin
  - artifact retention
        |
        v
Deployment
  - Firebase backend deploy
  - Google Play internal/closed/open/production tracks
  - monitoring and rollback
```

## Production Deployment Workflow

1. Merge approved code into `main`.
2. Confirm `environments/production/config.properties`.
3. Confirm GitHub environment secrets are present.
4. Run `test.yml` and review unit, lint, and backend build output.
5. Trigger `release.yml` manually with version name and version code.
6. Build a signed `productionRelease` app bundle.
7. Deploy Firebase rules, indexes, storage rules, and Cloud Functions.
8. Upload the AAB to Google Play internal testing first.
9. Promote through closed testing, open testing, and production after monitoring.
10. Watch Crashlytics, Performance Monitoring, Firebase Analytics, Firestore, Storage, and Cloud Functions dashboards.

## GitHub Actions Configuration

```text
.github/workflows/build.yml
.github/workflows/test.yml
.github/workflows/release.yml
```

Required release secrets:

```text
GOOGLE_SERVICES_JSON_STAGING
GOOGLE_SERVICES_JSON_PRODUCTION
ROLA_RELEASE_KEYSTORE_BASE64
ROLA_RELEASE_STORE_PASSWORD
ROLA_RELEASE_KEY_ALIAS
ROLA_RELEASE_KEY_PASSWORD
FIREBASE_SERVICE_ACCOUNT
GOOGLE_PLAY_SERVICE_ACCOUNT_JSON
```

Development builds can use `environments/dev/google-services.placeholder.json` for compile and non-release CI validation.

## Release Checklist

- Version code increased.
- Version name updated.
- Release notes prepared.
- App bundle builds successfully.
- R8 minification and resource shrinking enabled.
- Crashlytics mapping upload enabled.
- Firebase config injected from CI secret.
- Keystore decoded only in CI runner temp storage.
- Firestore and Storage rules reviewed.
- Backend Functions build passes.
- AI model registry has production and rollback entries.
- Room migrations tested.
- Privacy policy and data safety form updated.
- Google Play content rating confirmed.
- Internal testing release verified before promotion.

## Security Checklist

- No keystore, passwords, service-account files, or private API keys committed.
- Firebase API keys restricted in Google Cloud console.
- Firebase Auth and custom claims configured for admin/teacher roles.
- Firestore rules enforce user ownership and admin-only writes.
- Storage rules enforce role and file-size limits.
- Crash reports avoid raw learning content and personal identifiers.
- AI prompts avoid unnecessary personal data.
- Release builds use HTTPS endpoints only.
- Dependency review completed before production.
- Audit logs enabled for admin content, AI model, and institution actions.

## Maintenance Strategy

- Weekly dependency and vulnerability review.
- Monthly Firebase rules and index review.
- Monthly AI model performance comparison.
- Quarterly disaster recovery drill.
- Maintain separate dev, staging, and production Firebase projects.
- Keep staged rollout below 10% until crash-free sessions and latency are healthy.
- Use `contentVersions` and `aiModels` to roll back bad educational content or model releases.
- Monitor database migrations before increasing rollout.

## Long-Term Update Roadmap

- Add Google Play publishing automation after service-account approval.
- Add Firebase emulator rule tests to CI.
- Add Gradle Managed Devices for UI smoke tests.
- Add dependency vulnerability scanning.
- Add release health gates based on Crashlytics and Performance Monitoring.
- Add automated Firestore export scheduling.
- Add remote config flags for staged feature and AI model rollouts.
- Add school/institution deployment templates with SSO and rostering.

## Readiness Status

ROLA now has production flavors, secure signing preparation, CI/CD workflows, Firebase monitoring dependencies, release documentation, environment manifests, and remote AI model update planning. Final release readiness still requires a configured Android SDK in the build environment, real Firebase app configs, CI secrets, Play Console setup, and device validation on ARCore-supported hardware.
