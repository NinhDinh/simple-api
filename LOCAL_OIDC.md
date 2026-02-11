# Local OIDC Setup (Keycloak + Spring Resource Server)

## 1. Start Keycloak

```bash
docker compose up -d keycloak
```

Keycloak will be available at `http://localhost:8180`.

Admin credentials:
- username: `admin`
- password: `admin`

A realm and client are auto-imported from `deploy/keycloak/realm-local-dev.json`.

## 2. Run Spring Boot against local issuer

```bash
export OAUTH2_ISSUER_URI=http://localhost:8180/realms/local-dev
mvn spring-boot:run
```

## 3. Get an access token (client credentials)

Preconfigured client:
- client_id: `backend-caller`
- client_secret: `backend-caller-secret`

```bash
TOKEN=$(curl -s -X POST \
  "http://localhost:8180/realms/local-dev/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=backend-caller" \
  -d "client_secret=backend-caller-secret" | jq -r .access_token)

echo "$TOKEN"
```

If you do not have `jq`, use this fallback:

```bash
TOKEN=$(curl -s -X POST \
  "http://localhost:8180/realms/local-dev/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=backend-caller" \
  -d "client_secret=backend-caller-secret" | sed -n 's/.*"access_token":"\([^"]*\)".*/\1/p')
```

## 4. Call your API with bearer token

```bash
curl -i -H "Authorization: Bearer $TOKEN" http://localhost:8080/hello/world
```

## 5. Stop local IdP

```bash
docker compose down
```

## Production wiring

In production, keep the same app code and change only issuer configuration:

```bash
export OAUTH2_ISSUER_URI=https://<your-prod-idp>/<issuer-path>
```
