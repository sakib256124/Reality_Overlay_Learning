# ROLA CI/CD Guide

## Workflows

- `build.yml`: compiles and assembles the dev debug APK.
- `test.yml`: runs Android unit tests, lint, and backend TypeScript build.
- `release.yml`: creates signed staging or production AAB artifacts and deploys production Firebase backend resources.

## Pipeline

```text
Code Commit
  -> Build
  -> Unit Tests
  -> Integration Test Preparation
  -> Security Checks
  -> Release Build
  -> Deployment
```

## Versioning

Pass versions at release time:

```bash
./gradlew :app:bundleProductionRelease \
  -PROLA_VERSION_NAME=1.0.1 \
  -PROLA_VERSION_CODE=2
```

## Signing

Release signing uses environment variables only:

```text
ROLA_RELEASE_STORE_FILE
ROLA_RELEASE_STORE_PASSWORD
ROLA_RELEASE_KEY_ALIAS
ROLA_RELEASE_KEY_PASSWORD
```

Never commit keystores or passwords.
