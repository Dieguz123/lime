import {Component, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {switchMap, tap} from 'rxjs';
import {UserService} from '../../services/user.service';
import {RoomService} from '../../services/room.service';
import {SessionService} from '../../services/session.service';

@Component({
  selector: 'app-home',
  imports: [FormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {
  private userService = inject(UserService);
  private roomService = inject(RoomService);
  private session = inject(SessionService);
  private router = inject(Router);

  username = '';
  code = '';
  error = signal<string | null>(null);

  createRoom(): void {
    this.userService.create(this.username).pipe(
      tap(user => this.session.setUser(user)),
      switchMap(user => this.roomService.create(user.id))
    ).subscribe({
      next: room => this.router.navigate(['/room', room.inviteCode]),
      error: () => this.error.set('Something went wrong, try again')
    });
  }

  joinRoom(): void {
    this.userService.create(this.username).pipe(
      tap(user => this.session.setUser(user)),
      switchMap(user => this.roomService.join(this.code.toUpperCase(), user.id))
    ).subscribe({
      next: room => this.router.navigate(['/room', room.inviteCode]),
      error: (err) => this.error.set(
        err.status === 404 ? 'Room not found' :
          err.status === 409 ? 'Room is already full' :
            'Something went wrong, try again'
      )
    });
  }
}
