# AI Services

## Responsibilities

- Cloud-assisted object recognition fallback.
- AI tutor request handling.
- Translation request handling.
- AI model registry and rollout metadata.
- AI latency and error telemetry.

## Model Registry

Firestore collection: `aiModels`

```json
{
  "modelId": "object-detection-v2",
  "modelFamily": "object-detection",
  "version": "2.0",
  "status": "production",
  "accuracy": 0.96,
  "storagePath": "ai-models/object-detection/2.0/model.tflite",
  "minAppVersion": "1.0.0",
  "updatedAt": 0
}
```
