import {Injectable, inject} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {API_URL} from '../api';
import {Room} from '../models/room';

@Injectable({providedIn: 'root'})
export class RoomService {
  private http = inject(HttpClient);
  private apiUrl = `${API_URL}/rooms`;

  create(name: string, providerIds: number[]): Observable<Room> {
    return this.http.post<Room>(this.apiUrl, {name, providerIds});
  }

  get(code: string): Observable<Room> {
    return this.http.get<Room>(`${this.apiUrl}/${code}`);
  }

  updateProviders(code: string, providerIds: number[]): Observable<Room> {
    return this.http.put<Room>(`${this.apiUrl}/${code}/providers`, {providerIds});
  }

  join(code: string): Observable<Room> {
    return this.http.post<Room>(`${this.apiUrl}/${code}/join`, {});
  }

  mine(): Observable<Room[]> {
    return this.http.get<Room[]>(`${this.apiUrl}/mine`);
  }
}
