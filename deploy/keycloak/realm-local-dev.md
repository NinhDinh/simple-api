# realm-local-dev.json notes

- Realm: `local-dev`
- Client: `backend-caller` (confidential, client_credentials)
- Required API role: `read:hello`
- Token shape goal:
  - includes `realm_access.roles` with `read:hello`
  - empty `scope`
  - no `resource_access`

How it works:
- `defaultClientScopes` and `optionalClientScopes` are empty.
- `fullScopeAllowed` is `false`.
- `oidc-hardcoded-role-mapper` injects realm role `read:hello`.
- `oidc-usermodel-realm-role-mapper` exposes realm roles as `realm_access.roles`.

Swagger UI CORS:
- `webOrigins` allows `http://localhost:8080` and `http://127.0.0.1:8080`.
- `redirectUris` includes Swagger OAuth2 redirect URL for both hosts.
