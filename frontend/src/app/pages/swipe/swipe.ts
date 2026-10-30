import {Component, inject, OnInit, signal} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {SwipeService} from '../../services/swipe.service';
import {RoomService} from '../../services/room.service';
import {ProviderService} from '../../services/provider.service';
import {Film} from '../../models/film';
import {Provider} from '../../models/provider';
import {ProviderPicker} from '../../components/provider-picker/provider-picker';

@Component({
  selector: 'app-swipe',
  imports: [RouterLink, ProviderPicker],
  templateUrl: './swipe.html',
  styleUrl: './swipe.css'
})
export class Swipe implements OnInit {
  private route = inject(ActivatedRoute);
  private swipeService = inject(SwipeService);
  private roomService = inject(RoomService);
  private providerService = inject(ProviderService);

  code = this.route.snapshot.paramMap.get('code')!;

  roomProviders = signal<Provider[]>([]);
  allProviders = signal<Provider[]>([]);
  selectedProviders = signal<number[]>([]);
  editingProviders = signal(false);
  savingProviders = signal(false);
  providersError = signal<string | null>(null);

  film = signal<Film | null>(null);
  loading = signal(true);
  voting = signal(false);
  matchFilm = signal<Film | null>(null);
  flipped = signal(false);

  readonly posterBaseUrl = "https://image.tmdb.org/t/p/w500";
  readonly logoBaseUrl = "https://image.tmdb.org/t/p/w92";

  ngOnInit(): void {
    this.loadNext();
    this.roomService.get(this.code).subscribe(room => this.roomProviders.set(room.providers));
    this.providerService.getAll().subscribe(providers => this.allProviders.set(providers));
  }

  toggleEditProviders(): void {
    this.selectedProviders.set(this.roomProviders().map(provider => provider.id));
    this.providersError.set(null);
    this.editingProviders.update(value => !value);
  }

  saveProviders(): void {
    if (this.voting() || this.savingProviders()) return;

    this.savingProviders.set(true);
    this.providersError.set(null);
    this.roomService.updateProviders(this.code, this.selectedProviders()).subscribe({
      next: room => {
        this.roomProviders.set(room.providers);
        this.savingProviders.set(false);
        this.editingProviders.set(false);
        this.flipped.set(false);
        this.loadNext();
      },
      error: () => {
        this.savingProviders.set(false);
        this.providersError.set('Something went wrong, try again');
      }
    });
  }

  vote(liked: boolean): void {
    this.flipped.set(false);
    const film = this.film();
    if (!film || this.voting() || this.savingProviders()) return;

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
