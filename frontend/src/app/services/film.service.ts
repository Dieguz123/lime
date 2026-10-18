import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Film } from '../models/film';

@Injectable({ providedIn: 'root' })
export class FilmService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/films';

  getAll(): Observable<Film[]> {
    return this.http.get<Film[]>(this.apiUrl);
  }
}
