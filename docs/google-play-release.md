# Google Play Release Preparation

## App Bundle

Build production release:

```bash
./gradlew :app:bundleProductionRelease
```

Upload:

```text
app/build/outputs/bundle/productionRelease/*.aab
```

## Store Listing

- App name: ROLA
- Short description: AI and AR learning from real-world objects.
- Full description: ROLA helps learners scan objects, see AR explanations, hear guided narration, explore knowledge graphs, ask an AI tutor, translate content, complete quizzes, and track learning progress.
- Category: Education.
- Screenshots: phone, tablet if supported, AR scanning, quiz, tutor, knowledge map, dashboard.
- Privacy policy: required before production release.
- Content rating: complete in Play Console.
- Data safety: disclose Firebase Auth, analytics, crash reporting, learning history, and cloud sync.

## Release Tracks

```text
Internal Testing -> Closed Testing -> Open Testing -> Production Release
```

Promotion criteria:

- Internal: install, login, scan, quiz, tutor, sync smoke tests pass.
- Closed: crash-free sessions healthy, no migration failures.
- Open: user feedback reviewed, performance stable.
- Production: staged rollout with monitoring and rollback plan.
