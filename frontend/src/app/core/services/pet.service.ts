import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export interface Pet {
  id?: number;
  name: string;
  status: string;
  category?: {
    id: number;
    name: string;
  };
  photoUrls?: string[];
  tags?: {
    id: number;
    name: string;
  }[];
}

@Injectable({ providedIn: 'root' })
export class PetService {
  private baseUrl = `${environment.apiUrl}/api/pet`;

  constructor(private http: HttpClient) {}

  list(): Observable<Pet[]> {
    return this.http.get<any>(this.baseUrl).pipe(
      map(response => response.data || response)
    );
  }

  findByStatus(status: string): Observable<Pet[]> {
    const params = new HttpParams().set('status', status);
    return this.http.get<any>(`${this.baseUrl}/findByStatus`, { params }).pipe(
      map(response => response.data || response)
    );
  }

  get(petId: number): Observable<Pet> {
    return this.http.get<any>(`${this.baseUrl}/${petId}`).pipe(
      map(response => response.data || response)
    );
  }

  create(pet: Pet): Observable<Pet> {
    return this.http.post<any>(this.baseUrl, pet).pipe(
      map(response => response.data || response)
    );
  }

  update(petId: number, pet: Pet): Observable<Pet> {
    return this.http.put<any>(`${this.baseUrl}/${petId}`, pet).pipe(
      map(response => response.data || response)
    );
  }

  delete(petId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${petId}`);
  }
}
