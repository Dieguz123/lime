# Lime 🍋‍🟩

![CI](https://github.com/Dieguz123/lime/actions/workflows/ci.yml/badge.svg)

**Lime** is an app where you swipe through movies and say whether you like them or not.
You join a room with one person by sharing a link, and when you both like the same movie, it's a match!

Thanks to Lime, I will never argue with my girlfriend again about which movie to watch. 🍿

> 🚧 **Work in progress.** The MVP is complete: create a room, share the invite code, swipe and see your matches. Next: PostgreSQL, real movies from TMDB, and deploy.

## Tech stack

**Backend**

- Java 25
- Spring Boot 4
- Spring Data JPA
- H2 (in-memory database)
- Gradle
- JUnit 5

**Frontend**

- Angular 22
- TypeScript
- Signals

**CI**

- GitHub Actions: builds the backend and runs the tests on every push and pull request to `main`

## Getting started

Both the backend and the frontend must be running at the same time.

### Backend

```bash
./gradlew bootRun
```

The API starts on `http://localhost:8080`.

### Frontend

```bash
cd frontend
npm install
ng serve
```

The app starts on `http://localhost:4200`.

## Running the tests

```bash
./gradlew test
```

The tests are integration tests: they use `@SpringBootTest` to start the full Spring context with the H2 database,
and `@Transactional` to roll back every test, so each one starts from clean data.

## API endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/films` | Returns the list of all movies |
| GET | `/api/films/{id}` | Returns the movie with the given id, or `404 Not Found` if it doesn't exist |
| POST | `/api/users` | Creates a user, returns `201 Created` |
| GET | `/api/users/{id}` | Returns the user with the given id, or `404 Not Found` if it doesn't exist |
| POST | `/api/rooms` | Creates a room with an invite code, returns `201 Created`, or `404 Not Found` if the creator doesn't exist |
| GET | `/api/rooms/{code}` | Returns the room with the given invite code, or `404 Not Found` if it doesn't exist |
| POST | `/api/rooms/{code}/join` | Joins a room, `404 Not Found` if the room or user doesn't exist, `409 Conflict` if the user can't join |
| POST | `/api/rooms/{code}/swipes` | Votes a movie and returns whether it's a match, `404 Not Found` if the room, user or movie doesn't exist, `409 Conflict` if the user isn't a member or already voted the movie |
| GET | `/api/rooms/{code}/next-film?userId={id}` | Returns the next movie the user hasn't voted yet, `204 No Content` if there are none left, `404 Not Found` if the room or user doesn't exist, `409 Conflict` if the user isn't a member |
| GET | `/api/rooms/{code}/matches` | Returns the movies both members liked, or `404 Not Found` if the room doesn't exist |

## Project structure

### Backend

The code is organized in layers:

- **controller**: handles HTTP requests and decides the response (200, 404, ...)
- **service**: contains the business logic, such as looking up a movie by id
- **repository**: Spring Data JPA interfaces that read and write data in the database
- **model**: the classes that represent the data, such as `Film`
- **dto**: the request and response bodies of the API, such as `SwipeRequest`
- **exception**: custom exceptions and the global handler that turns them into HTTP status codes (404, 409)
- **config**: the CORS configuration, which allows requests from the Angular dev server (`http://localhost:4200`)

The controller receives the service through constructor-based dependency injection, which keeps the layers separate.

### Frontend

The Angular app lives in the `frontend` folder:

- **pages**: `home` (create or join a room), `swipe` (vote the movies) and `matches` (the movies you both liked)
- **services**: call the backend REST API
- **models**: TypeScript interfaces matching the backend DTOs
- **session**: the current user is saved in `localStorage`, so it survives a page reload

## Roadmap

- [x] Rooms shared through an invite code
- [x] Swipes and match logic
- [x] Mobile-friendly web interface
- [ ] PostgreSQL with Docker Compose
- [ ] Real movie data from the TMDB API
- [ ] Deploy online
- [ ] Installable PWA
- [ ] Real-time match notifications (WebSocket)
- [ ] Authentication with Spring Security (hashed passwords)
