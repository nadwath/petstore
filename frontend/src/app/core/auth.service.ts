import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private at?: string;
  private baseUrl = `${environment.apiUrl}/api/auth`;

  constructor(private http: HttpClient) {}

  accessToken() {
    return this.at;
  }

  refreshToken() {
    return localStorage.getItem('rt');
  }

  getPublicKey() {
    return this.http.get<any>(`${this.baseUrl}/public-key`);
  }

  login(username: string, encryptedPassword: string) {
    return this.http.post<any>(`${this.baseUrl}/login`, { username, encryptedPassword }).pipe(
      tap(response => {
        const t = response.data || response;
        this.at = t.accessToken;
        localStorage.setItem('rt', t.refreshToken);
      })
    );
  }

  refresh() {
    const rt = this.refreshToken();
    if (!rt) throw new Error('no refresh');
    return this.http.post<any>(`${this.baseUrl}/refresh`, { refreshToken: rt }).pipe(
      tap(response => {
        const t = response.data || response;
        this.at = t.accessToken;
      })
    );
  }

  logout() {
    return this.http.post(`${this.baseUrl}/logout`, {}).pipe(
      tap(() => {
        this.at = undefined;
        localStorage.removeItem('rt');
      })
    );
  }
}
