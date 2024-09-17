import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth/auth.service';
import { UserService } from '../../../services/user/user.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule
  ],
  providers: [
    AuthService,
    UserService
  ],
  templateUrl: './header.component.html'
})
export class HeaderComponent {
  isLoggedIn = false;
  username = '-';

  constructor(
    private router: Router,
    private authService: AuthService,
    private userService: UserService
  ) { }

  ngOnInit() {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.getProfile();
  }

  navigateToLogin() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  getProfile() {
    this.userService.getProfile().subscribe({
      next: (res: any) => {
        this.username = res.name;
      },
      error: (err) => {
        console.error(err);
      }
    });
  }
}
