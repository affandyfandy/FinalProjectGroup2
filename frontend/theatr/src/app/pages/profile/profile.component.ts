import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { UserService } from '../../services/user/user.service';
import { User } from '../../model/user.model';
import { AuthService } from '../../services/auth/auth.service';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule
  ],
  providers: [
    UserService,
    AuthService
  ],
  templateUrl: './profile.component.html',
})
export class ProfileComponent {

  user = {} as User;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.getProfile();
  }

  getProfile() {
    this.userService.getProfile().subscribe({
      next: (res: any) => {
        this.user = res;
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
