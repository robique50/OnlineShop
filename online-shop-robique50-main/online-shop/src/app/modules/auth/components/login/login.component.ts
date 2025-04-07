import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../../services/auth.service';
import { finalize } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  error = '';
  loading = false;
  returnUrl = '/products';

  constructor(
    private authService: AuthService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  ngOnInit() {
    this.returnUrl =
      this.activatedRoute.snapshot.queryParams['returnUrl'] || '/products';
  }

  login(): void {
    if (this.loginForm.invalid) {
      Object.keys(this.loginForm.controls).forEach((key) => {
        const control = this.loginForm.get(key);
        if (control?.invalid) {
          control.markAsTouched();
        }
      });
      return;
    }

    this.loading = true;
    this.error = '';

    this.authService
      .login(this.loginForm.value)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (user) => {
          this.authService.currentUserSubject.next(user);
          this.router.navigate(['/products']);
        },
        error: (error) => {
          console.error('Login error:', error);
          this.error = error.error || 'Login failed. Please try again.';
        },
      });
  }

  hasError(controlName: string, errorType: string): boolean {
    const control = this.loginForm.get(controlName);
    return (control?.touched && control?.hasError(errorType)) || false;
  }
}
