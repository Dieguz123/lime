import {Injectable, inject} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {API_URL} from '../api';
import {Provider} from '../models/provider';

@Injectable({providedIn: 'root'})
export class ProviderService {
  private http = inject(HttpClient);
  private apiUrl = `${API_URL}/providers`;

  getAll(): Observable<Provider[]> {
    return this.http.get<Provider[]>(this.apiUrl);
  }
}
