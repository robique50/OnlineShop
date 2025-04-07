import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, catchError, Observable, switchMap, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { Router } from '@angular/router';
import { LoginRequest, User } from '../modules/shared/types/user.types';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  public currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    this.checkAuthStatus();
  }

  private checkAuthStatus(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      this.currentUserSubject.next(null);
      return;
    }

    this.loadUserProfile().subscribe({
      next: (user) => this.currentUserSubject.next(user),
      error: () => {
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
        switchMap(() => this.loadUserProfile()),
        tap((user) => {
          this.currentUserSubject.next(user);
          const redirectUrl =
            localStorage.getItem('redirectUrl') || '/products';
          localStorage.removeItem('redirectUrl');
          this.router.navigate([redirectUrl]);
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
    return !!token && !!this.currentUserSubject.value;
  }

  public isAdmin(): boolean {
    return this.currentUserSubject.value?.role === 'ADMIN';
  }

  public isCustomer(): boolean {
    return this.currentUserSubject.value?.role === 'CUSTOMER';
  }
}