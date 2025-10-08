import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export interface User {
  id?: number;
  username: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  password?: string;
  phone?: string;
  userStatus?: number;
  roleIds?: number[];
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private baseUrl = `${environment.apiUrl}/api/user`;

  constructor(private http: HttpClient) {}

  list(): Observable<User[]> {
    return this.http.get<any>(this.baseUrl).pipe(
      map(response => response.data || response)
    );
  }

  get(username: string): Observable<User> {
    return this.http.get<any>(`${this.baseUrl}/${username}`).pipe(
      map(response => response.data || response)
    );
  }

  create(user: User): Observable<User> {
    return this.http.post<any>(this.baseUrl, user).pipe(
      map(response => response.data || response)
    );
  }

  update(username: string, user: User): Observable<User> {
    return this.http.put<any>(`${this.baseUrl}/${username}`, user).pipe(
      map(response => response.data || response)
    );
  }

  delete(username: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${username}`);
  }
}
