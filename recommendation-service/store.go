package main

import (
	"context"

	"github.com/jackc/pgx/v5/pgxpool"
)

const queryInsertFilm = `
INSERT INTO films (film_id, genre_ids) VALUES ($1,$2)
ON CONFLICT (film_id) DO UPDATE SET genre_ids = EXCLUDED.genre_ids;`

const queryInsertSwipe = `
INSERT INTO swipes (room_id, film_id, user_id, liked, occurred_at) VALUES ($1,$2,$3,$4,$5)
ON CONFLICT (room_id, film_id, user_id) DO UPDATE SET liked = EXCLUDED.liked, occurred_at = EXCLUDED.occurred_at;`

type Store struct {
	pool *pgxpool.Pool
}

func NewStore(pool *pgxpool.Pool) *Store {
	return &Store{pool: pool}
}

func (s *Store) UpsertFilm(ctx context.Context, film *FilmImported) error {
	_, err := s.pool.Exec(ctx, queryInsertFilm, film.FilmID, film.GenreIDs)
	return err
}

func (s *Store) UpsertSwipe(ctx context.Context, swipe *SwipeRecorded) error {
	_, err := s.pool.Exec(ctx, queryInsertSwipe, swipe.RoomID, swipe.FilmID, swipe.UserID, swipe.Liked, swipe.OccurredAt)
	return err
}
