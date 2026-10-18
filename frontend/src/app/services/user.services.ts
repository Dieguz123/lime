import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import {API_URL} from '../api';

@Injectable({ providedIn: 'root' })
export class UserService {
  private http = inject(HttpClient);
  private apiUrl = `${API_URL}/rooms`;

  create(username: string): Observable<User>{
     return this.http.post<User>(this.apiUrl , { username })
  }
}
