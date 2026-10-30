export interface Film {
  id: number;
  name: string;
  releaseYear: number | null;
  providers: { id: number; name: string; logoPath: string | null }[];
  genres: { id: number; name : string} [];
  tmdbId: number;
  overview: string | null;
  posterPath: string | null;
}
