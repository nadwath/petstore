import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export interface Tag {
  id: number;
  name: string;
}

@Injectable({ providedIn: 'root' })
export class TagService {
  private baseUrl = `${environment.apiUrl}/api/tag`;

  constructor(private http: HttpClient) {}

  list(): Observable<Tag[]> {
    return this.http.get<any>(this.baseUrl).pipe(
      map(response => response.data || response)
    );
  }

  get(id: number): Observable<Tag> {
    return this.http.get<any>(`${this.baseUrl}/${id}`).pipe(
      map(response => response.data || response)
    );
  }
}
