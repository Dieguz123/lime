export interface SwipeRequest {
  userId: number;
  filmId: number;
  liked: boolean
}

export interface SwipeResponse {
  match: boolean;
}
