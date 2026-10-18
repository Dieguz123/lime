# Lime frontend 🍋‍🟩

The web interface of Lime, built with Angular 22. It's designed mobile-first: create or join a room,
swipe through the movies and see your matches.

## Running it

The frontend needs the backend running on `http://localhost:8080` (see the [main README](../README.md)).

```bash
npm install
ng serve
```

Then open `http://localhost:4200`.

## Folder structure

```
src/app/
├── pages/      the screens of the app
│   ├── home/      create or join a room
│   ├── swipe/     vote the movies and see when it's a match
│   └── matches/   the movies you both liked
├── services/   call the backend REST API and keep the session in localStorage
└── models/     TypeScript interfaces matching the backend DTOs
```

The base URL of the API is defined in `src/app/api.ts`.
