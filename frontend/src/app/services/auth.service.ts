import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, switchMap, tap } from 'rxjs';
import { API_URL } from '../api';
import { User } from '../models/user';
import { LoginResponse } from '../models/auth';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = `${API_URL}/auth`;
  private readonly tokenKey = 'lime-token';

  readonly token = signal<string | null>(localStorage.getItem(this.tokenKey));
  readonly user = signal<User | null>(null);
  readonly isLoggedIn = computed(() => this.token() !== null);

  register(username: string, password: string): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/register`, { username, password });
  }

  login(username: string, password: string): Observable<User> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { username, password }).pipe(
      tap(response => this.saveToken(response.token)),
      switchMap(() => this.loadMe())
    );
  }

  loadMe(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/me`).pipe(
      tap(user => this.user.set(user))
    );
  }

  logout(): void {
    this.token.set(null);
    this.user.set(null);
    localStorage.removeItem(this.tokenKey);
  }

  private saveToken(token: string): void {
    this.token.set(token);
    localStorage.setItem(this.tokenKey, token);
  }
}
