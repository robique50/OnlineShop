import {
  HttpErrorResponse,
  HttpInterceptorFn,
  HttpRequest,
  HttpHandlerFn,
  HttpEvent,
} from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';

export const authInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
  const router = inject(Router);
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
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          console.error('Interceptor caught 401 error. Redirecting to login.');
          localStorage.removeItem('token');
          localStorage.removeItem('cartItems');
          router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }

  console.warn('No token found for non-public route:', req.url);
  return next(req);
};
