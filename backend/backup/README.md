# ROLA Backup And Recovery

Use scheduled Firestore exports and versioned Storage paths for disaster recovery.

## Backup Scope

- Firestore: users, learning history, quiz results, knowledge graph, research updates, analytics rollups, AI model descriptors.
- Storage: object images, 3D models, TFLite model files, educational resources.
- Functions: source is versioned in the repository and deployed through CI.

## Schedule

```text
Firestore export: daily
Storage inventory: daily
AI model descriptor snapshot: before every production model rollout
Recovery drill: quarterly
```

## Restore Order

1. Restore Firebase Auth users from account export if needed.
2. Restore Firestore export.
3. Verify Storage buckets and object generations.
4. Redeploy security rules and indexes.
5. Redeploy Cloud Functions.
6. Release or roll back Android app track.
