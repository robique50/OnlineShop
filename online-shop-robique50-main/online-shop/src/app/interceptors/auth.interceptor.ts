import {
  HttpErrorResponse,
  HttpInterceptorFn,
  HttpRequest,
  HttpHandlerFn,
  HttpEvent,
} from '@angular/common/http';
import { inject, Injector } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, catchError, throwError, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthService } from '../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

export const authInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
  const injector = inject(Injector);
  const router = inject(Router);
  const snackBar = inject(MatSnackBar);
  const token = localStorage.getItem('token');

  const publicPaths = [
    `${environment.apiUrl}/auth/login`,
    `${environment.apiUrl}/auth/register`,
  ];

  const isPublicPath = publicPaths.some((path) => req.url.includes(path));

  if (isPublicPath) {
    return next(req);
  }

  if (token) {
    const modifiedReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`),
    });

    return next(modifiedReq).pipe(
      tap(
        (event) => {
        },
        (error) => {
        }
      ),
      catchError((error: HttpErrorResponse) => {       

        if (error.status === 401) {
          try {
            const authService = injector.get(AuthService);
            authService.logout();
            snackBar.open(
              'Your session has expired. Please log in again.',
              'Close',
              { duration: 4000, panelClass: ['warning-snackbar'] }
            );
          } catch (injectionError) {
             router.navigate(['/login']);
          }
        } else {
        }
        return throwError(() => error);
      })
    );
  }

  return next(req);
}
