# simple-api

Bootstrap Spring Boot API project to experiment with JWT-based API security.

Current goal:
- Run a secured API as an OAuth2 Resource Server
- Use local Keycloak as OIDC issuer
- Test `client_credentials` flow and role-based authorization (`read:hello`)

Planned next step:
- Add automated tests

## Tech Stack

- Java + Maven
- Spring Boot 4
- Spring Security OAuth2 Resource Server (JWT)
- Springdoc OpenAPI + Swagger UI
- Keycloak (Podman Compose)

## Project Structure

- `src/main/java/org/example/ninh/HelloApplication.java`: Spring Boot entrypoint
- `src/main/java/org/example/ninh/controller/HelloController.java`: secured sample endpoint
- `src/main/java/org/example/ninh/config/SecurityConfig.java`: JWT security config + role mapping
- `src/main/java/org/example/ninh/config/OpenApiConfig.java`: OpenAPI OAuth2 documentation
- `deploy/keycloak/realm-local-dev.json`: local realm/client import
- `docker-compose.yml`: local Keycloak service
- `hello.http`: JetBrains HTTP Client requests (token + API call)

## Prerequisites

- Java (JDK 17+)
- Maven (`mvn`)
- Podman with compose support

## Local Setup

### 1. Start Keycloak

```bash
podman compose -f docker-compose.yml up -d keycloak
```

Keycloak runs on: `http://127.0.0.1:8180`

### 2. Run the API (local profile)

```bash
SPRING_PROFILES_ACTIVE=local mvn spring-boot:run
```

The local profile sets issuer URI to local Keycloak.

API runs on: `http://localhost:8080`

## Authentication Model

Configured client in local realm:
- `client_id`: `backend-caller`
- `client_secret`: `backend-caller-secret`
- grant type: `client_credentials`

Expected token shape:
- `realm_access.roles` contains `read:hello`
- `scope` is empty
- no `resource_access`

## Test the API

### Option A: Swagger UI

- Open: `http://localhost:8080/swagger-ui.html`
- Click `Authorize`
- Use OAuth2 `client_credentials` with:
  - client id: `backend-caller`
  - client secret: `backend-caller-secret`
- Call `GET /hello/{name}`

`/hello/{name}` requires role: `read:hello`.

### Option B: JetBrains HTTP Client

Use `hello.http`:
1. Request token
2. Call secured endpoint with bearer token

## Useful Commands

Re-import realm config from scratch:

```bash
podman compose -f docker-compose.yml down -v
podman compose -f docker-compose.yml up -d keycloak
```

Compile app:

```bash
mvn -DskipTests compile
```

Build container image:

```bash
mvn -DskipTests clean package
podman build -t simple-api:latest .
```

Run API + Keycloak together:

```bash
podman compose -f docker-compose.yml up -d keycloak api
```

Note:
- API validates JWT issuer against the internal service URL (`http://keycloak:8080/...`).
- Swagger UI token request uses the host URL (`http://127.0.0.1:8180/...`) so browser authorization works.

## Notes

- This is a development bootstrap, not production hardening.
- Keycloak realm comments/documentation are in `deploy/keycloak/realm-local-dev.md`.
