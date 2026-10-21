import {Component, inject, OnInit, signal} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {SwipeService} from '../../services/swipe.service';
import {Film} from '../../models/film';

@Component({
  selector: 'app-swipe',
  imports: [RouterLink],
  templateUrl: './swipe.html',
  styleUrl: './swipe.css'
})
export class Swipe implements OnInit {
  private route = inject(ActivatedRoute);
  private swipeService = inject(SwipeService);

  code = this.route.snapshot.paramMap.get('code')!;

  film = signal<Film | null>(null);
  loading = signal(true);
  voting = signal(false);
  matchFilm = signal<Film | null>(null);
  flipped = signal(false);

  readonly posterBaseUrl = "https://image.tmdb.org/t/p/w500";

  ngOnInit(): void {
    this.loadNext();
  }

  vote(liked: boolean): void {
    this.flipped.set(false);
    const film = this.film();
    if (!film || this.voting()) return;

    this.voting.set(true);
    this.swipeService.swipe(this.code, { filmId: film.id, liked })
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
    this.swipeService.nextFilm(this.code).subscribe(film => {
      this.film.set(film);
      this.loading.set(false);
    });
  }

  toggleFlip(): void {
    this.flipped.update(value => !value);
  }
}
