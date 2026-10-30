import {Component, inject, OnInit, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {RoomService} from '../../services/room.service';
import {ProviderService} from '../../services/provider.service';
import {Provider} from '../../models/provider';
import {ProviderPicker} from '../../components/provider-picker/provider-picker';

@Component({
  selector: 'app-create-room',
  imports: [FormsModule, RouterLink, ProviderPicker],
  templateUrl: './create-room.html',
  styleUrl: './create-room.css'
})
export class CreateRoom implements OnInit {
  private roomService = inject(RoomService);
  private providerService = inject(ProviderService);
  private router = inject(Router);

  name = signal('');
  providers = signal<Provider[]>([]);
  selectedProviders = signal<number[]>([]);
  creating = signal(false);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.providerService.getAll().subscribe({
      next: providers => this.providers.set(providers),
      error: () => this.error.set('Could not load the streaming platforms')
    });
  }

  createRoom(): void {
    const name = this.name().trim();
    if (!name || this.creating()) return;

    this.creating.set(true);
    this.error.set(null);
    this.roomService.create(name, this.selectedProviders()).subscribe({
      next: room => this.router.navigate(['/room', room.inviteCode]),
      error: () => {
        this.creating.set(false);
        this.error.set('Something went wrong, try again');
      }
    });
  }
}
