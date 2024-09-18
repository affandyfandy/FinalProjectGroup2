import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { AuthService } from '../../../services/auth/auth.service';
import { UserService } from '../../../services/user/user.service';
import { RouterConfig } from '../../../config/app.constants';

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
export class HeaderComponent implements OnInit {
  isLoggedIn = false;
  username = '-';

  constructor(
    private router: Router,
    private authService: AuthService,
    private userService: UserService
  ) { }

  ngOnInit() {
    console.log('HeaderComponent');

    this.authService.isLoggedIn$.subscribe((status) => {
      this.isLoggedIn = status;
      this.getProfile();
    });

    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.checkLoginStatus();
      }
    });
  }

  navigateToLogin() {
    this.router.navigate([RouterConfig.LOGIN.link]);
  }

  navigateToProfile() {
    this.router.navigate([RouterConfig.PROFILE.link]);
  }

  navigateToBookings() {
    this.router.navigate([RouterConfig.BOOKINGS.link]);
  }

  navigateToHome() {
    this.router.navigate([RouterConfig.HOME.link]);
  }

  navigateToAdminBookings() {
    this.router.navigate([RouterConfig.ADMIN_BOOKINGS.link]);
  }

  navigateToAdminMovies() {
    this.router.navigate([RouterConfig.MOVIES.link]);
  }

  navigateToAdminSchedule() {
    this.router.navigate([RouterConfig.ADMIN_SCHEDULES.link]);
  }

  navigateToAdminStudio() {
    this.router.navigate([RouterConfig.STUDIOS.link]);
  }

  getProfile() {
    if (this.isLoggedIn) {
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

  checkLoginStatus() {
    this.isLoggedIn = this.authService.isLoggedIn();
  }
}
