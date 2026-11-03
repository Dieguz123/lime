package main

import (
	"context"
	"encoding/json"
	"log/slog"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/twmb/franz-go/pkg/kgo"
)

const (
	topicFilms  = "lime.films"
	topicSwipes = "lime.swipes"
)

type FilmImported struct {
	FilmID      int64   `json:"filmId"`
	TmdbID      int64   `json:"tmdbId"`
	Title       string  `json:"title"`
	ReleaseYear *int    `json:"releaseYear"`
	GenreIDs    []int64 `json:"genreIds"`
	ProviderIDs []int64 `json:"providerIds"`
}

type SwipeRecorded struct {
	RoomID     int64     `json:"roomId"`
	UserID     int64     `json:"userId"`
	FilmID     int64     `json:"filmId"`
	Liked      bool      `json:"liked"`
	OccurredAt time.Time `json:"occurredAt"`
}

func main() {
	brokers := getenv("KAFKA_BROKERS", "localhost:9092")
	databaseURL := getenv("DATABASE_URL", "postgres://lime:lime@localhost:5432/recommendations")

	client, err := kgo.NewClient(
		kgo.SeedBrokers(brokers),
		kgo.ConsumerGroup("recommendation-service"),
		kgo.ConsumeTopics(topicFilms, topicSwipes),
		kgo.ConsumeResetOffset(kgo.NewOffset().AtStart()),
	)
	if err != nil {
		slog.Error("cannot create Kafka client", "err", err)
		os.Exit(1)
	}
	defer client.Close()

	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()

	pool, err := pgxpool.New(ctx, databaseURL)
	if err != nil {
		slog.Error("cannot create database connection pool", "err", err)
		os.Exit(1)
	}
	defer pool.Close()

	if err := pool.Ping(ctx); err != nil {
		slog.Error("cannot ping database", "err", err)
		os.Exit(1)
	}
	slog.Info("connected to database")
	slog.Info("recommendation-service started", "brokers", brokers)

	for {
		fetches := client.PollFetches(ctx)
		if ctx.Err() != nil || fetches.IsClientClosed() {
			slog.Info("shutting down")
			return
		}

		fetches.EachError(func(topic string, partition int32, err error) {
			slog.Error("fetch error", "topic", topic, "partition", partition, "err", err)
		})

		fetches.EachRecord(handle)
	}
}

func handle(record *kgo.Record) {
	switch record.Topic {
	case topicFilms:
		var event FilmImported
		if err := json.Unmarshal(record.Value, &event); err != nil {
			slog.Warn("invalid film event", "err", err)
			return
		}
		slog.Info("film imported", "filmId", event.FilmID, "title", event.Title, "genres", event.GenreIDs)

	case topicSwipes:
		var event SwipeRecorded
		if err := json.Unmarshal(record.Value, &event); err != nil {
			slog.Warn("invalid swipe event", "err", err)
			return
		}
		slog.Info("swipe recorded", "roomId", event.RoomID, "filmId", event.FilmID, "liked", event.Liked)
	}
}

func getenv(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}
