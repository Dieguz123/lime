import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { SwipeService } from '../../services/swipe.service';
import { Film } from '../../models/film';

@Component({
  imports: [RouterLink],
  selector: 'app-matches',
  styleUrl: './matches.css',
  templateUrl: './matches.html',
})
export class Matches implements OnInit {
  private route = inject(ActivatedRoute);
  private swipeService = inject(SwipeService);

  code = this.route.snapshot.paramMap.get('code')!;
  matches = signal<Film[]>([]);

  readonly posterBaseUrl = 'https://image.tmdb.org/t/p/w185';

  ngOnInit(): void {
    this.swipeService.matches(this.code)
      .subscribe(films => this.matches.set(films));
  }
}
