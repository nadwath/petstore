import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService){}
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.auth.accessToken();
    const cloned = token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` }}) : req;
    return next.handle(cloned).pipe(
      catchError((err: HttpErrorResponse) => {
        if (err.status === 401 && this.auth.refreshToken()) {
          return this.auth.refresh().pipe(switchMap(() => next.handle(req.clone({
            setHeaders: { Authorization: `Bearer ${this.auth.accessToken()}` }
          }))));
        }
        return throwError(() => err);
      })
    );
  }
}
