import { Component } from '@angular/core';
import { AuthService } from './core/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  template: `
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
      <a class="navbar-brand" routerLink="/pets">
        <i class="bi bi-shop"></i> Petstore
      </a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ms-auto">
          <li class="nav-item" *ngIf="isLoggedIn()">
            <a class="nav-link" routerLink="/pets" routerLinkActive="active">Pets</a>
          </li>
          <li class="nav-item" *ngIf="isLoggedIn() && isAdmin()">
            <a class="nav-link" routerLink="/users" routerLinkActive="active">Users</a>
          </li>
          <li class="nav-item" *ngIf="!isLoggedIn()">
            <a class="nav-link" routerLink="/auth/login" routerLinkActive="active">Login</a>
          </li>
          <li class="nav-item" *ngIf="isLoggedIn()">
            <button class="btn btn-link nav-link" (click)="logout()">Logout</button>
          </li>
        </ul>
      </div>
    </div>
  </nav>
  <div class="container mt-4">
    <router-outlet></router-outlet>
  </div>`
})
export class AppComponent {
  constructor(private auth: AuthService, private router: Router) {}

  isLoggedIn(): boolean {
    return !!this.auth.accessToken();
  }

  isAdmin(): boolean {
    return this.auth.isAdmin();
  }

  logout(): void {
    this.auth.logout().subscribe({
      next: () => {
        this.router.navigate(['/auth/login']);
      },
      error: () => {
        // Clear tokens even if server call fails
        this.auth.accessToken();
        localStorage.removeItem('rt');
        this.router.navigate(['/auth/login']);
      }
    });
  }
}
