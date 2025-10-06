import { Component } from '@angular/core';
@Component({
  selector: 'app-root',
  template: `
  <nav>
    <a routerLink="/pets">Pets</a> |
    <a routerLink="/auth/login">Login</a>
  </nav>
  <router-outlet></router-outlet>`
})
export class AppComponent {}
