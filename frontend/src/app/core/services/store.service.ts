import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export interface Order {
  id?: number;
  petId: number;
  quantity: number;
  shipDate?: string;
  status?: string;
  complete?: boolean;
}

export interface Inventory {
  petId: number;
  quantity: number;
}

@Injectable({ providedIn: 'root' })
export class StoreService {
  private baseUrl = `${environment.apiUrl}/api/store`;

  constructor(private http: HttpClient) {}

  getInventory(): Observable<Map<string, number>> {
    return this.http.get<any>(`${this.baseUrl}/inventory`).pipe(
      map(response => response.data || response)
    );
  }

  placeOrder(order: Order): Observable<Order> {
    return this.http.post<any>(`${this.baseUrl}/order`, order).pipe(
      map(response => response.data || response)
    );
  }

  getOrder(orderId: number): Observable<Order> {
    return this.http.get<any>(`${this.baseUrl}/order/${orderId}`).pipe(
      map(response => response.data || response)
    );
  }

  deleteOrder(orderId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/order/${orderId}`);
  }

  cancelOrder(orderId: number): Observable<Order> {
    return this.http.post<any>(`${this.baseUrl}/order/${orderId}/cancel`, {}).pipe(
      map(response => response.data || response)
    );
  }

  approveOrder(orderId: number): Observable<Order> {
    return this.http.post<any>(`${this.baseUrl}/order/${orderId}/approve`, {}).pipe(
      map(response => response.data || response)
    );
  }

  addInventoryStock(petId: number, quantity: number): Observable<Inventory> {
    const params = new HttpParams().set('quantity', quantity.toString());
    return this.http.post<any>(`${this.baseUrl}/inventory/${petId}/add`, null, { params }).pipe(
      map(response => response.data || response)
    );
  }

  getInventoryByPetId(petId: number): Observable<Inventory> {
    return this.http.get<any>(`${this.baseUrl}/inventory/${petId}`).pipe(
      map(response => response.data || response)
    );
  }
}
