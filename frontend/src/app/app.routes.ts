import { Routes } from '@angular/router';
import { AuthGuard } from './core/auth.guard';
export const APP_ROUTES: Routes = [
  { path: 'auth', loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule) },
  { path: 'pets', loadChildren: () => import('./features/pets/pets.module').then(m => m.PetsModule), canActivate: [AuthGuard] },
  { path: '', pathMatch: 'full', redirectTo: 'pets' }
];
