import { Component } from '@angular/core';
import { AuthService } from '../../core/auth.service';

@Component({
  template: `
  <h2>Login</h2>
  <form (ngSubmit)="login()">
    <input [(ngModel)]="u" name="u" placeholder="username">
    <input [(ngModel)]="p" name="p" type="password" placeholder="password">
    <button>Login</button>
  </form>`
})
export class LoginComponent {
  u=''; p='';
  constructor(private auth: AuthService){}
  async login(){
    const pub = await this.auth.getPublicKey().toPromise();
    const keyData = Uint8Array.from(atob(pub.key), c => c.charCodeAt(0));
    const key = await window.crypto.subtle.importKey('spki', keyData, { name: 'RSA-OAEP', hash: 'SHA-256' }, false, ['encrypt']);
    const enc = new TextEncoder().encode(this.p);
    const cip = await window.crypto.subtle.encrypt({ name: 'RSA-OAEP' }, key, enc);
    const b64 = btoa(String.fromCharCode(...new Uint8Array(cip)));
    this.auth.login(this.u, b64).subscribe();
  }
}
