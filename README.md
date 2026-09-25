# CUE

[![CI](https://github.com/robbyrp/CUE/actions/workflows/ci.yml/badge.svg)](https://github.com/robbyrp/CUE/actions/workflows/ci.yml)

A Letterboxd-style web app for **theatre** — track plays you've seen, build a watchlist, search across titles/directors/locations, and leave reviews.

## Gallery
<img width="2833" height="1531" alt="screenshot2" src="https://github.com/user-attachments/assets/a92be4cb-d855-4e7b-bf88-86998c8591e6" />
<img width="2833" height="1484" alt="screenshot4" src="https://github.com/user-attachments/assets/ceb46044-ad5a-4258-ae2d-da9807c56173" />



## Stack

- **Backend:** Spring Boot 3.5, Java 21, Spring Data JPA / Hibernate, Spring Security (JWT, self-implemented — no third-party auth SDK), PostgreSQL, Maven.
- **Frontend:** React 19, TypeScript, Vite, React Router, Axios, SCSS Modules.
- **Testing:** JUnit 5 + Mockito for service-layer unit tests; JUnit 5 + Testcontainers (real PostgreSQL, not H2) for repository-layer integration tests.
- **CI:** GitHub Actions — unit tests (Surefire) and integration tests (Failsafe + Testcontainers) run as two separate steps on every push/PR.

## Diagram
<a href="https://gitdiagram.com/robbyrp/cue" > <img width="5162" height="6198" alt="diagram" src="https://github.com/user-attachments/assets/2971414f-5830-499d-8ae7-21c6ffdd8bc2" />
</a>

## Architecture

The backend follows a standard layered structure: `Controller → Service → Repository`, with Record-based DTOs, injectable `@Component` mappers, and centralized exception handling via a single `@RestControllerAdvice`. Authentication is a manually implemented JWT flow (`OncePerRequestFilter` + `Spring Security`, `BCrypt`-hashed passwords) instead of a third-party auth provider, and role/ownership checks (USER/ADMIN, review authorship) live in the service layer. Search is accent- and case-insensitive (Postgres `unaccent` extension) across title, director, and location, with paginated results (`Page<T>` / `Pageable`) throughout.

## Credits

The images used for the performance cover pictures are from <a href="unsplash.com"> unsplash.com </a> .

## Testing

101 tests total, split into two Maven phases so a fast local loop never needs Docker:

```bash
./mvnw test        # 48 unit tests (Mockito, no Docker)
./mvnw verify       # + 53 integration tests (real PostgreSQL via Testcontainers)
```

Integration tests cover custom JPQL/native queries, pagination edge cases (partial last page, page past the end, sorting through associations), and DB-level behavior that a mocked repository can't catch — unique constraints, soft-delete filtering (`@SQLDelete`/`@SQLRestriction`), and one real bug a test caught before it shipped: two entities sharing the same DB constraint name, which silently meant only one of them was actually enforced.

## Getting Started

### Backend

Requires Docker (with `docker compose`) and JDK 21.

```bash
cd backend
cp .env.example .env
# edit .env: set POSTGRES_PASSWORD and generate JWT_SECRET_KEY, e.g. `openssl rand -hex 32`

./mvnw spring-boot:run
```

`.env` is read both by `docker compose` (for the Postgres container, auto-started on app boot via `spring-boot-docker-compose`) and by the Spring Boot app itself — one file, no separate setup. It's gitignored; `.env.example` documents the required keys.

### Frontend

```bash
cd frontend
npm install
npm run dev
```
