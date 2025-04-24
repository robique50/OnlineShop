import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../../services/auth.service';
import { Router } from '@angular/router';
import { RegisterRequest } from '../../../shared/types/user.types';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-register',
  standalone: false, 
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  isLoading = false;
  availableRoles: string[] = ['CUSTOMER', 'ADMIN'];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.maxLength(50)]],
      username: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(50),
        ],
      ],
      emailAddress: [
        '',
        [Validators.required, Validators.email, Validators.maxLength(100)],
      ],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(6),
          Validators.maxLength(100),
        ],
      ],
      role: ['', [Validators.required]], // Default to empty, user must select
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched(); // Mark fields to show validation errors
      return;
    }

    this.isLoading = true;
    const registerRequest: RegisterRequest = this.registerForm.value;

    this.authService
      .register(registerRequest)
      .pipe(
        finalize(() => (this.isLoading = false)) // Ensure loading is set to false on complete/error
      )
      .subscribe({
      });
  }

  get firstName() {
    return this.registerForm.get('firstName');
  }
  get lastName() {
    return this.registerForm.get('lastName');
  }
  get username() {
    return this.registerForm.get('username');
  }
  get emailAddress() {
    return this.registerForm.get('emailAddress');
  }
  get password() {
    return this.registerForm.get('password');
  }
  get role() {
    return this.registerForm.get('role');
  }

  navigateToLogin(): void {
    this.router.navigate(['/login']);
  }

  hasError(controlName: string, errorType: string): boolean {
    const control = this.registerForm.get(controlName);
    return (control?.touched && control?.hasError(errorType)) || false;
  }
}
