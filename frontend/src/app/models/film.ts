export interface Film {
  id: number;
  name: string;
  releaseYear: number | null;
  platform: string | null;
  genres: { id: number; name : string} [];
  tmdbId: number;
  overview: string | null;
  posterPath: string | null;
}
