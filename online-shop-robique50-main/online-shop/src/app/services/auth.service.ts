import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {
  BehaviorSubject,
  catchError,
  Observable,
  switchMap,
  tap,
  throwError,
} from 'rxjs';
import { environment } from '../../environments/environment';
import { Router } from '@angular/router';
import {
  LoginRequest,
  RegisterRequest,
  User,
} from '../modules/shared/types/user.types';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  public currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.checkAuthStatus();
  }

  private checkAuthStatus(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      this.currentUserSubject.next(null);
      return;
    }
    this.loadUserProfile().subscribe({
      next: (user) => {
        this.currentUserSubject.next(user);
      },
      error: (err) => {
        this.clearAuth(); 
      },
    });
  }

  private clearAuth(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('cartItems');
    this.currentUserSubject.next(null);
  }

  public login(loginRequest: LoginRequest): Observable<any> {
    return this.http
      .post(`${environment.apiUrl}/auth/login`, loginRequest, {
        responseType: 'text',
      })
      .pipe(
        tap((token) => {
          localStorage.setItem('token', token);
        }),
        switchMap(() => {
          return this.loadUserProfile();
        }),
        tap((user) => {
          this.currentUserSubject.next(user);
          const redirectUrl =
            localStorage.getItem('redirectUrl') || '/products';
          localStorage.removeItem('redirectUrl');
          this.router.navigate([redirectUrl]);
        }),
        catchError((error) => {
          const errorMessage =
            error?.error || 'Login failed. Please check your credentials.';
          this.snackBar.open(errorMessage, 'Close', {
            duration: 3000,
            panelClass: ['error-snackbar'],
          });
          return throwError(() => error);
        })
      );
  }

  public register(registerRequest: RegisterRequest): Observable<any> {
    return this.http
      .post(`${environment.apiUrl}/auth/register`, registerRequest)
      .pipe(
        tap(() => {
          this.snackBar.open(
            'Registration successful! Please log in.',
            'Close',
            { duration: 3000, panelClass: ['success-snackbar'] }
          );
          this.router.navigate(['/login']);
        }),
        catchError((error) => {
          const errorMessage =
            error?.error || 'Registration failed. Please try again.';
          this.snackBar.open(errorMessage, 'Close', {
            duration: 3000,
            panelClass: ['error-snackbar'],
          });
          return throwError(() => error);
        })
      );
  }

  public logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('cartItems');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  public loadUserProfile(): Observable<User> {
    return this.http.get<User>(`${environment.apiUrl}/auth/profile`);
  }

  public isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    return !!token;
  }

  public isAdmin(): boolean {
    const user = this.currentUserSubject.value;
    return user?.role === 'ADMIN';
  }

  public isCustomer(): boolean {
    const user = this.currentUserSubject.value;
    return user?.role === 'CUSTOMER';
  }
}
