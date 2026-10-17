# Lime 🍋‍🟩

**Lime** is an app where you swipe through movies and say whether you like them or not.
You join a room with one person by sharing a link, and when you both like the same movie, it's a match!

Thanks to Lime, I will never argue with my girlfriend again about which movie to watch. 🍿

> 🚧 **Work in progress.** The backend MVP is complete: users, rooms, swipes and matches. The frontend is coming next.

## Tech stack

- Java 25
- Spring Boot 4
- Gradle
- JUnit 5
- Spring Data JPA
- H2 (in-memory database)

## Getting started

```bash
./gradlew bootRun
```

The app starts on `http://localhost:8080`.

## Running the tests

```bash
./gradlew test
```

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

The code is organized in layers:

- **controller**: handles HTTP requests and decides the response (200, 404, ...)
- **service**: contains the business logic, such as looking up a movie by id
- **repository**: Spring Data JPA interfaces that read and write data in the database
- **model**: the classes that represent the data, such as `Film`
- **dto**: the request and response bodies of the API, such as `SwipeRequest`
- **exception**: custom exceptions and the global handler that turns them into HTTP status codes (404, 409)

The controller receives the service through constructor-based dependency injection.
This keeps the layers separate, and the business logic can be tested without starting Spring.

## Roadmap

- [ ] PostgreSQL instead of H2
- [x] Rooms shared through an invite link
- [x] Swipes and match logic
- [ ] Real movie data from the TMDB API
- [ ] Mobile-friendly web interface
- [ ] Authentication with Spring Security (hashed passwords)
