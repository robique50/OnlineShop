import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const token = localStorage.getItem('token');

  if (token) {
    const modifiedReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`),
    });

    return next(modifiedReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          localStorage.removeItem('token');
          localStorage.removeItem('cartItems');
          router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }

  if (!token && !req.url.includes('/auth/login')) {
    localStorage.setItem('redirectUrl', router.url);
    router.navigate(['/login']);
    return throwError(() => new Error('No authentication token'));
  }

  return next(req);
};
