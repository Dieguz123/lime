import { Injectable, signal } from '@angular/core';
import { User } from '../models/user';

@Injectable({ providedIn: 'root' })
export class SessionService {
  private readonly key = 'lime-user';
  readonly user = signal<User | null>(this.load());

  setUser(user: User): void {
    this.user.set(user);
    localStorage.setItem(this.key, JSON.stringify(user));
  }

  private load(): User | null {
    const saved = localStorage.getItem(this.key);
    return saved ? JSON.parse(saved) : null;
  }
}
