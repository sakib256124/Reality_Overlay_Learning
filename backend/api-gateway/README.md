# API Gateway

All mobile cloud traffic can be routed through the HTTPS Cloud Function `api`.

## REST Endpoints

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

Every protected endpoint requires a Firebase ID token. Admin routes require a custom claim role of `admin` or `educator`.

## API Security

- `Authorization: Bearer <Firebase ID token>` is validated at the gateway.
- User-scoped routes derive `userId` from the token, not the request body.
- Admin routes are role-gated by Firebase custom claims.
- API responses include no-store and basic browser hardening headers.
- Payloads are length-limited before Firestore writes.
