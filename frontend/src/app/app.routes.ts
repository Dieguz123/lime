import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Swipe } from './pages/swipe/swipe';
import { Matches } from './pages/matches/matches';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'room/:code', component: Swipe },
  { path: 'room/:code/matches', component: Matches },
  { path: '**', redirectTo: '' }
];
