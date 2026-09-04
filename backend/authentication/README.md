# Authentication

ROLA cloud APIs expect Firebase ID tokens in the `Authorization` header:

```text
Authorization: Bearer FIREBASE_ID_TOKEN
```

The backend verifies tokens through Firebase Admin SDK and reads optional custom claims:

- `learner`: default role.
- `educator`: may manage educational content.
- `admin`: may manage content, models, storage metadata, and analytics.

Security boundaries are also enforced in Firestore and Storage rules.
