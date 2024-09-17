import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule
  ],
  providers: [AuthService],
  templateUrl: './header.component.html'
})
export class HeaderComponent {
  isLoggedIn = false;

  constructor(private router: Router, private authService: AuthService,) { }

  navigateToLogin() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
