import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private at?: string;
  constructor(private http: HttpClient){}
  accessToken(){ return this.at; }
  refreshToken(){ return localStorage.getItem('rt'); }

  getPublicKey(){ return this.http.get<any>('/api/auth/public-key'); }
  login(username: string, encryptedPassword: string){
    return this.http.post<any>('/api/auth/login', {username, encryptedPassword}).pipe(tap(t => {
      this.at = t.accessToken; localStorage.setItem('rt', t.refreshToken);
    }));
  }
  refresh(){
    const rt = this.refreshToken(); if (!rt) throw new Error('no refresh');
    return this.http.post<any>('/api/auth/refresh', {refreshToken: rt}).pipe(tap(t => this.at = t.accessToken));
  }
  logout(){
    return this.http.post('/api/auth/logout', {}).pipe(tap(() => { this.at = undefined; localStorage.removeItem('rt'); }));
  }
}
