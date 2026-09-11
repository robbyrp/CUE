# CUE

[![CI](https://github.com/robbyrp/CUE/actions/workflows/ci.yml/badge.svg)](https://github.com/robbyrp/CUE/actions/workflows/ci.yml)

A Letterboxd-style mobile app for **theatre** — track plays you've seen, build a
watchlist of plays to catch, discover theatres, and plan meet-and-greets with actors.

## Repository Layout

This is a single repository with plain folders:

| Folder      | What lives here                                            | Owner             |
| ----------- | ---------------------------------------------------------- | ----------------- |
| `frontend/` | The React Native + Expo mobile app                         | Frontend dev      |
| `backend/`  | The Spring Boot + PostgreSQL API                           | Backend engineer  |
| `docs/`     | Architecture notes & the Frontend↔Backend API contract     | Shared            |

## Tech Stack

- **Frontend:** React Native, Expo, Expo Router, NativeWind (Tailwind), TypeScript,
  Zustand (client state), TanStack Query (server state).
- **Backend:** Spring Boot 3.5.15 (Java 25, Maven), Spring Data JPA / Hibernate,
  PostgreSQL, Lombok, Bean Validation. _Planned:_ Firebase Auth, Cloudflare R2,
  Sentry.

## Getting Started

> The frontend app is built up incrementally. Concrete setup steps land here as
> `frontend/` is scaffolded.

```bash
# Frontend (Expo app)
cd frontend
npm install
npx expo start
```

```bash
# Backend (Spring Boot API) — requires Docker + JDK 25
cd backend
docker compose -f src/main/resources/docker-compose.yaml up -d   # local Postgres
./mvnw spring-boot:run
```

## Documentation

- `DESIGN.md` — design system & tokens (created during bootstrap, not yet present).
- `docs/` — architecture notes & the API contract (**planned, not yet created**).
