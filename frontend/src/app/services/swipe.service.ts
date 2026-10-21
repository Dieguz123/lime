import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../api';
import { Film } from '../models/film';
import { SwipeRequest, SwipeResponse } from '../models/swipe';

@Injectable({ providedIn: 'root' })
export class SwipeService {
  private http = inject(HttpClient);

  swipe(code: string, request: SwipeRequest): Observable<SwipeResponse> {
    return this.http.post<SwipeResponse>(`${API_URL}/rooms/${code}/swipes`, request);
  }

  nextFilm(code: string): Observable<Film | null> {
    return this.http.get<Film | null>(`${API_URL}/rooms/${code}/next-film`);
  }

  matches(code: string): Observable<Film[]> {
    return this.http.get<Film[]>(`${API_URL}/rooms/${code}/matches`);
  }
}
