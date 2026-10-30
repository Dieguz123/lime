import {Component, inject, OnInit, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {RoomService} from '../../services/room.service';
import {ProviderService} from '../../services/provider.service';
import {AuthService} from '../../services/auth.service';
import {Room} from '../../models/room';
import {Provider} from '../../models/provider';
import {ProviderPicker} from '../../components/provider-picker/provider-picker';

@Component({
  selector: 'app-home',
  imports: [FormsModule, RouterLink, ProviderPicker],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  auth = inject(AuthService);
  private roomService = inject(RoomService);
  private providerService = inject(ProviderService);
  private router = inject(Router);

  rooms = signal<Room[]>([]);
  providers = signal<Provider[]>([]);
  selectedProviders = signal<number[]>([]);
  loading = signal(true);
  code = '';
  error = signal<string | null>(null);

  ngOnInit(): void {
    if (this.auth.user() == null) {
      this.auth.loadMe().subscribe();
    }
    this.roomService.mine().subscribe(rooms => {
        this.rooms.set(rooms);
        this.loading.set(false);
      }
    )
    this.providerService.getAll().subscribe({
      next: providers => this.providers.set(providers),
      error: () => this.error.set('Could not load the streaming platforms')
    });
  }

  otherMember(room: Room): string | null {
    const me = this.auth.user();

    if (!room.partner || !me) {
      return null;
    }

    return room.creator.id === me.id
      ? room.partner.username
      : room.creator.username;
  }

  createRoom(): void {
    this.roomService.create(this.selectedProviders()).subscribe({
      next: room => this.router.navigate(['/room', room.inviteCode]),
      error: () => this.error.set('Something went wrong, try again')
    });
  }

  joinRoom(): void {
    this.roomService.join(this.code.toUpperCase()).subscribe({
      next: room => this.router.navigate(['/room', room.inviteCode]),
      error: (err) => this.error.set(
        err.status === 404 ? 'Room not found' :
          err.status === 409 ? 'Room is already full' :
            'Something went wrong, try again'
      )
    });
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
