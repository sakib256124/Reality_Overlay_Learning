# ROLA Environments

Environment manifests describe deployment targets only. They must not contain secrets, signing passwords, service-account keys, or private Firebase configuration.

```text
environments/
  dev/
  staging/
  production/
```

Use CI secrets for:

- `GOOGLE_SERVICES_JSON_DEV`
- `GOOGLE_SERVICES_JSON_STAGING`
- `GOOGLE_SERVICES_JSON_PRODUCTION`
- `ROLA_RELEASE_KEYSTORE_BASE64`
- `ROLA_RELEASE_STORE_PASSWORD`
- `ROLA_RELEASE_KEY_ALIAS`
- `ROLA_RELEASE_KEY_PASSWORD`
- `FIREBASE_SERVICE_ACCOUNT`
- `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON`
