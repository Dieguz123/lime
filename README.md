# Lime 🍋‍🟩

![CI](https://github.com/Dieguz123/lime/actions/workflows/ci.yml/badge.svg)

**Live at https://lime.diegomarchesini.com**

**Lime** is a web app to pick a movie to watch together.
You create an account, open a room and share its invite code with the other person.
Then you both swipe through the most popular movies of the moment, with poster, year and plot:
when you both like the same movie, it's a match, and it's saved in the room's list of matches.
So when it's time to choose what to watch, you already know what you both want to see.

Thanks to Lime, I will never argue with my girlfriend again about which movie to watch. 🍿

## Tech stack

**Backend**

- Java 25, Spring Boot 4, Gradle
- Spring Data JPA and Hibernate, with PostgreSQL
- Flyway for versioned database migrations
- Spring Security with JWT authentication and BCrypt-hashed passwords
- TMDB API, called with Spring's `RestClient`, to import real movies
- JUnit 5 for the tests, with H2 as an in-memory database

**Frontend**

- Angular 22 and TypeScript
- Signals for the state, RxJS for the HTTP calls
- An HTTP interceptor that adds the JWT to every request, and a route guard for the pages that need login

**Infrastructure**

- Docker and Docker Compose
- Caddy as reverse proxy, with automatic HTTPS
- AWS EC2, with the domain on Cloudflare
- GitHub Actions for CI/CD: tests on every pull request, automatic deploy on every merge to `main`

## Getting started

### Requirements

- Java 25
- Node.js 24 and npm
- Docker
- A free [TMDB](https://www.themoviedb.org) account and its API Read Access Token, to import the movies

### 1. Database

```bash
docker compose up -d
```

This starts PostgreSQL on `localhost:5432`. Flyway creates the tables when the backend starts.

### 2. Backend

Set the TMDB token as an environment variable, then start the app:

```bash
export TMDB_TOKEN=your-token
./gradlew bootRun
```

On Windows PowerShell use `$env:TMDB_TOKEN="your-token"` instead of `export`.
The API starts on `http://localhost:8080` and imports the most popular movies from TMDB on startup.

### 3. Frontend

```bash
cd frontend
npm install
npm start
```

The app starts on `http://localhost:4200`.

## Running the tests

```bash
./gradlew test
```

No database or TMDB token is needed: the tests run on their own.

- **Service tests** (`@SpringBootTest`) start the full Spring context on an in-memory H2 database.
  The tables are created by the same Flyway migrations used in production, with Hibernate in `validate` mode,
  so a broken migration fails the tests before it reaches the server.
  Every test runs in a transaction that is rolled back at the end (`@Transactional`), so each one starts from clean data.
  They cover registration and login, rooms, swipes, matches and the access rules for non-members.
- **TMDB client test**: a plain unit test that binds a `MockRestServiceServer` to the `RestClient`,
  so the TMDB response is simulated and no real HTTP call is made.

The TMDB import on startup is disabled in the tests with `tmdb.import.enabled=false`.

## API endpoints

All endpoints need an `Authorization: Bearer <token>` header, except the ones marked as public.
A missing or invalid token returns `401 Unauthorized`.

### Auth and users

| Method | Path | Description |
|---|---|---|
| POST | `/api/auth/register` | **Public.** Creates an account, `201 Created`, `400` if the data isn't valid, `409 Conflict` if the username is taken |
| POST | `/api/auth/login` | **Public.** Returns a JWT, or `401 Unauthorized` if the username or password is wrong |
| GET | `/api/auth/me` | Returns the logged-in user |
| GET | `/api/users/{id}` | Returns a user, or `404 Not Found` |

### Rooms

The user is always taken from the token, never from the request.
A user who isn't a member of the room gets `403 Forbidden`.

| Method | Path | Description |
|---|---|---|
| POST | `/api/rooms` | Creates a room with an invite code, `201 Created` |
| GET | `/api/rooms/mine` | Returns the rooms of the logged-in user |
| GET | `/api/rooms/{code}` | Returns the room, `404 Not Found` if it doesn't exist |
| POST | `/api/rooms/{code}/join` | Joins the room, `404 Not Found` if it doesn't exist, `409 Conflict` if it's full or you're already in it |
| POST | `/api/rooms/{code}/swipes` | Votes a movie (`filmId`, `liked`) and returns whether it's a match, `409 Conflict` if already voted |
| GET | `/api/rooms/{code}/next-film` | Returns the next movie to vote, `204 No Content` if there are none left |
| GET | `/api/rooms/{code}/matches` | Returns the movies both members liked |

### Movies and health

| Method | Path | Description |
|---|---|---|
| GET | `/api/films` | Returns all the movies |
| GET | `/api/films/{id}` | Returns a movie, or `404 Not Found` |
| GET | `/api/health` | **Public.** Returns `OK`, used to check that the backend is up |

## Project structure

### Backend

The code is organized in layers:

- **controller**: handles HTTP requests and decides the response status
- **service**: the business logic, such as the match rules and who can access a room
- **repository**: Spring Data JPA interfaces that read and write the database
- **model**: the JPA entities, such as `Film`, `Room` and `Swipe`
- **dto**: request and response bodies. Entities are never returned directly, so for example password hashes never leave the server
- **exception**: custom exceptions and a global handler that turns them into status codes (401, 403, 404, 409)
- **config**: security (JWT, BCrypt and access rules), the helper that reads the current user from the token, and CORS for local development
- **tmdb**: the TMDB client, its DTOs and the importer that loads the popular movies on startup

The database schema is managed by Flyway, with the migrations in `src/main/resources/db/migration`.

### Frontend

The Angular app lives in the `frontend` folder:

- **pages**: `login` (sign up and log in), `home` (your rooms, create or join one), `swipe` (vote the movies, tap a card to read the plot) and `matches`
- **services**: one per area of the API, plus `AuthService`, which handles login and stores the JWT
- **auth interceptor**: adds the token to every request, and sends you back to the login page if it has expired
- **auth guard**: protects the pages that need you to be logged in
- **environments**: the API URL for development (`localhost:8080`) and production (`/api`)

## Roadmap

- [x] Rooms shared through an invite code, swipes and matches
- [x] PostgreSQL with Docker Compose and Flyway migrations
- [x] Real movies from the TMDB API
- [x] Accounts with Spring Security, JWT and BCrypt
- [x] Deploy on AWS with automatic HTTPS and CI/CD
- [ ] Installable PWA
- [ ] Real-time match notifications (WebSocket)
- [ ] Filters by genre, year and streaming platform
- [ ] Integration tests on a real PostgreSQL with Testcontainers
