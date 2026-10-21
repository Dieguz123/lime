import {Component, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {switchMap} from 'rxjs';

@Component({
  imports: [FormsModule],
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrl: './login.css'
})

export class Login {
  private router = inject(Router);
  private authService = inject(AuthService);

  username = '';
  password = '';

  loading = signal(false);
  error = signal<string | null>(null);


  login(): void {
    this.loading.set(true);
    this.error.set(null);

    this.authService.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/']),
      error: err => {
        this.loading.set(false);
        this.error.set(err.status === 401
          ? 'Wrong username or password'
          : 'Something went wrong, try again')
      }
    });
  }

  register(): void {
    this.loading.set(true);
    this.error.set(null);

    this.authService.register(this.username, this.password).pipe(
      switchMap(() => this.authService.login(this.username, this.password))
    ).subscribe({
      next: () => this.router.navigate(['/']),
      error: err => {
        this.loading.set(false);
        if (err.status === 409) {
          this.error.set('Username already taken');
        } else if (err.status === 400) {
          this.error.set('Password must be at least 8 characters');
        } else {
          this.error.set('Something went wrong try again');
        }
      }
    })
  }
}
