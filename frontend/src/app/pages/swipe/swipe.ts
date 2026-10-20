import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { SwipeService } from '../../services/swipe.service';
import { SessionService } from '../../services/session.service';
import { Film } from '../../models/film';

@Component({
  selector: 'app-swipe',
  imports: [RouterLink],
  templateUrl: './swipe.html',
  styleUrl: './swipe.css'
})
export class Swipe implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private swipeService = inject(SwipeService);
  private session = inject(SessionService);

  code = this.route.snapshot.paramMap.get('code')!;
  user = this.session.user();

  film = signal<Film | null>(null);
  loading = signal(true);
  voting = signal(false);
  matchFilm = signal<Film | null>(null);
  flipped = signal(false);

  readonly posterBaseUrl = "https://image.tmdb.org/t/p/w500";

  ngOnInit(): void {
    if (!this.user) {
      this.router.navigate(['/']);
      return;
    }
    this.loadNext();
  }

  vote(liked: boolean): void {
    this.flipped.set(false);
    const film = this.film();
    if (!film || !this.user || this.voting()) return;

    this.voting.set(true);
    this.swipeService.swipe(this.code, { userId: this.user.id, filmId: film.id, liked })
      .subscribe(response => {
        if (response.match) {
          this.matchFilm.set(film);
        }
        this.voting.set(false);
        this.loadNext();
      });
  }

  closeMatch(): void {
    this.matchFilm.set(null);
  }

  copyCode(): void {
    navigator.clipboard.writeText(this.code);
  }

  private loadNext(): void {
    this.swipeService.nextFilm(this.code, this.user!.id).subscribe(film => {
      this.film.set(film);
      this.loading.set(false);
    });
  }

  toggleFlip(): void {
    this.flipped.update(value => !value);
  }
}
