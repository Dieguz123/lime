import { Component, OnInit, inject, signal } from '@angular/core';
import { FilmService } from './services/film.service';
import { Film } from './models/film';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  private filmService = inject(FilmService);
  films = signal<Film[]>([]);

  ngOnInit(): void {
    this.filmService.getAll().subscribe(films => this.films.set(films));
  }
}
