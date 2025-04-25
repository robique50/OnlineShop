import { Injectable } from '@angular/core';
import { Router, CanActivate, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  public canActivate():
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    if (this.authService.isAuthenticated()) {
      return true;
    }

    localStorage.setItem('redirectUrl', this.router.url);

    this.snackBar.open('Please log in to access this page.', 'Close', {
      duration: 4000,
      panelClass: ['warning-snackbar'],
    });
    this.authService.logout();

    this.router.navigate(['/login']);
    return false;
  }
}
