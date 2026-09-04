# Storage Layout

```text
objects/{objectId}/images/{version}/{fileName}
objects/{objectId}/models/{version}/{fileName}
ai-models/{modelFamily}/{version}/model.tflite
education-resources/{languageCode}/{fileName}
user-uploads/{userId}/{fileName}
```

Use metadata documents in `storageAssets` for version tracking, checksums, CDN URLs, and rollout state.
