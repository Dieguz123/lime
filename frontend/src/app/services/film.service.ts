import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Film } from '../models/film';

@Injectable({ providedIn: 'root' })
export class FilmService {
  private http = inject(HttpClient);
  private apiUrl = '${API_URL}/films';

  getAll(): Observable<Film[]> {
    return this.http.get<Film[]>(this.apiUrl);
  }
}
