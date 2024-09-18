import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { UserService } from '../../services/user/user.service';
import { User } from '../../model/user.model';
import { AuthService } from '../../services/auth/auth.service';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  providers: [
    UserService,
    AuthService
  ],
  templateUrl: './profile.component.html',
})
export class ProfileComponent {

  user = {} as User;
  tempUsername = '';
  isAdmin = this.authService.isAdmin();
  isShowAlert = false;
  alertMessage = '';
  isAlertSuccess = true;

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
        this.tempUsername = res.name;
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  logout() {
    this.authService.logout();
    this.closeLogoutModal();
    this.router.navigate(['/']);
  }

  updateProfile() {
    this.userService.updateProfile({ newName: this.user.name! }).subscribe({
      next: () => {
        this.tempUsername = this.user.name!;
        this.showAlert('Profile updated successfully', true);
      },
      error: (err) => {
        this.showAlert('Failed to update your profile', false);
      }
    });
  }

  showLogoutModal() {
    const modal = document.getElementById('logout-modal') as HTMLDialogElement;
    modal.showModal();
  }

  closeLogoutModal() {
    const modal = document.getElementById('logout-modal') as HTMLDialogElement;
    modal.close();
  }

  isButtonDisabled(): boolean {
    return this.user.name === this.tempUsername || this.user.name === '' || !this.user.name;
  }

  showAlert(message: string, success: boolean) {
    this.isAlertSuccess = success;
    this.alertMessage = message;
    this.isShowAlert = true;
    setTimeout(() => {
      this.isShowAlert = false;
    }, 3000);
  }
}
