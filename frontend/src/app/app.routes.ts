import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Swipe } from './pages/swipe/swipe';
import { Matches } from './pages/matches/matches';
import {Login} from './pages/login/login';
import {authGuard} from './auth.guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: '', component: Home, canActivate: [authGuard] },
  { path: 'room/:code', component: Swipe, canActivate: [authGuard] },
  { path: 'room/:code/matches', component: Matches, canActivate: [authGuard] },
  { path: '**', redirectTo: '' }
];
