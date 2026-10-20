export interface Film {
  id: number;
  name: string;
  releaseYear: number | null;
  platform: string | null;
  genre: string | null;
  tmdbId: number;
  overview: string | null,
  posterPath: string | null
}
