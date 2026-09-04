# Admin Management

Admin workflows are intentionally separated from learner APIs.

## Capabilities

- Add or update educational objects.
- Upload object images and 3D models to Firebase Storage.
- Register AI model files and production/staging status.
- Review analytics and learning trends.
- Manage educator/admin-only content changes.

## Role Model

Admin operations require a Firebase custom claim:

```json
{
  "role": "admin"
}
```

Educator workflows can use:

```json
{
  "role": "educator"
}
```
