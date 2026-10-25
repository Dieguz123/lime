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