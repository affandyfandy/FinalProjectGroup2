import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { UserService } from '../../services/user/user.service';
import { ChangePasswordDTO, TopUpDTO, User } from '../../model/user.model';
import { AuthService } from '../../services/auth/auth.service';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { FormsModule, NgForm } from '@angular/forms';
import { PriceFormatPipe } from '../../core/pipes/price-format/price-format.pipe';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    PriceFormatPipe
  ],
  providers: [
    UserService,
    AuthService
  ],
  templateUrl: './profile.component.html',
})
export class ProfileComponent {

  @ViewChild('passwordForm') passwordForm: NgForm = {} as NgForm;

  user = {} as User;
  tempUsername = '';
  isAdmin = this.authService.isAdmin();
  isShowAlert = false;
  alertMessage = '';
  isAlertSuccess = true;

  changePasswordDTO = {} as ChangePasswordDTO;

  topUpDTO: TopUpDTO = { balance: 0 };
  amountList = [10000, 20000, 50000, 100000, 200000, 250000];

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.getProfile();
    this.setUpDTO();
  }

  getProfile() {
    this.userService.getProfile().subscribe({
      next: (res: any) => {
        this.user = res;
        this.tempUsername = res.name;
      },
      error: (err) => {
        this.showAlert('Failed to get your profile: ' + err.error.message, false);
      }
    });
  }

  setUpDTO() {
    this.changePasswordDTO.newPassword = '';
    this.changePasswordDTO.oldPassword = '';
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
        this.showAlert('Failed to update your profile: ' + err.error.message, false);
      }
    });
  }

  topUp() {
    this.userService.topUpBalance(this.topUpDTO).subscribe({
      next: (res: any) => {
        this.closeTopUpModal();
        this.user.balance = res.balance ?? this.user.balance;
        this.showAlert('Top Up Success!', true);
      },
      error: (err) => {
        this.closeTopUpModal();
        this.showAlert('Top Up Failed: ' + err.error.message, false);
      }
    });
  }

  changePassword() {
    console.log('BODY:', this.changePasswordDTO);

    this.userService.changePassword(this.changePasswordDTO).subscribe({
      next: () => {
        this.closeChangePasswordModal();
        this.showAlert('Password changed successfully', true);
      },
      error: (err) => {
        this.showAlert('Failed to change your password: ' + err.error.message, false);
      },

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

  showChangePasswordModal() {
    const modal = document.getElementById('change-password-modal') as HTMLDialogElement;
    modal.showModal();
  }

  closeChangePasswordModal() {
    this.passwordForm.resetForm();
    const modal = document.getElementById('change-password-modal') as HTMLDialogElement;
    modal.close();
    this.setUpDTO();
    this.user.confirmPassword = '';
  }

  showTopUpModal() {
    const modal = document.getElementById('top-up-modal') as HTMLDialogElement;
    modal.showModal();
  }

  closeTopUpModal() {
    this.topUpDTO.balance = 0;
    const modal = document.getElementById('top-up-modal') as HTMLDialogElement;
    modal.close();
  }

  isButtonDisabled(): boolean {
    return this.user.name === this.tempUsername || this.user.name === '' || !this.user.name;
  }

  isUpdatePasswordButtonDisabled(): boolean {
    return (this.changePasswordDTO.oldPassword.length < 8) ||
      (this.changePasswordDTO.newPassword.length < 8) ||
      ((this.user.confirmPassword?.length ?? 0) < 8) ||
      (this.changePasswordDTO.newPassword !== this.user.confirmPassword);
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
