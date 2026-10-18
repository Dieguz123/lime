import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../api';
import { Room } from '../models/room';

@Injectable({ providedIn: 'root' })
export class RoomService {
  private http = inject(HttpClient);
  private apiUrl = `${API_URL}/rooms`;

  create(creatorId: number): Observable<Room> {
    return this.http.post<Room>(this.apiUrl, { creatorId });
  }

  join(code: string, userId: number): Observable<Room> {
    return this.http.post<Room>(`${this.apiUrl}/${code}/join`, { userId });
  }
}
