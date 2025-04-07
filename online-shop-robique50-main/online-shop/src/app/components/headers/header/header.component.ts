import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { Observable } from 'rxjs';
import { User } from '../../../modules/shared/types/user.types';

@Component({
  selector: 'app-header',
  standalone: false,

  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent {
  currentUser$: Observable<User | null>;

  constructor(private authService: AuthService) {
    this.currentUser$ = this.authService.currentUser$;
  }

  logout(): void {
    this.authService.logout();
  }
}
