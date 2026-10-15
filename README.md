# Lime 🍋‍🟩

**Lime** is an app where you swipe through movies and say whether you like them or not.
You join a room with one or more people by sharing a link, and when everyone likes the same movie, it's a match!

Thanks to Lime, I will never argue with my girlfriend again about which movie to watch. 🍿

> 🚧 **Work in progress.** The backend currently exposes a first set of endpoints for movies.

## Tech stack

- Java 25
- Spring Boot 4
- Gradle
- JUnit 5
- Spring data JPA

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

## Project structure

The code is organized in layers:

- **controller**: handles HTTP requests and decides the response (200, 404, ...)
- **service**: contains the business logic, such as looking up a movie by id
- **model**: the classes that represent the data, such as `Film`

The controller receives the service through constructor-based dependency injection.
This keeps the layers separate, and the business logic can be tested without starting Spring.

## Roadmap

- [ ] PostgreSQL database instead of the in-memory list
- [ ] Rooms shared through an invite link
- [ ] Swipes and match logic
- [ ] Real movie data from the TMDB API
- [ ] Mobile-friendly web interface
- [ ]Authentication with Spring Security (hashed passwords)