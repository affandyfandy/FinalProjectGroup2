import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { AuthService } from '../../../services/auth/auth.service';
import { UserService } from '../../../services/user/user.service';
import { RouterConfig } from '../../../config/app.constants';
import { TruncateNamePipe } from '../../../core/pipes/truncate-name/truncate-name.pipe';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule,
    TruncateNamePipe
  ],
  providers: [
    AuthService,
    UserService
  ],
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {
  currentUrl = '';
  isLoggedIn = false;
  username = '-';
  isAdmin = this.authService.isAdmin();

  constructor(
    private router: Router,
    private authService: AuthService,
    private userService: UserService
  ) { }

  ngOnInit() {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.checkLoginStatus();
        this.currentUrl = event.url;
        this.isAdmin = this.authService.isAdmin();
        this.getProfile();
      }
    });
  }

  isSectionActive(section: string): boolean {
    return this.currentUrl === ('/' + section);
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
