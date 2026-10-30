import {Provider} from './provider';

export interface Film {
  id: number;
  name: string;
  releaseYear: number | null;
  providers: Provider[];
  genres: { id: number; name : string} [];
  tmdbId: number;
  overview: string | null;
  posterPath: string | null;
}
