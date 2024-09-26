import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { Router } from '@angular/router';
import { MessageConstants, RouterConfig } from '../../config/app.constants';
import { FormsModule, NgForm } from '@angular/forms';
import { User } from '../../model/user.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  providers: [
    AuthService
  ],
  templateUrl: './login.component.html',
})
export class LoginComponent {

  @ViewChild('authForm') authForm: NgForm = {} as NgForm;

  isRegister = false;
  user: User = {} as User;

  isShowAlert = false;
  alertMessage = '';
  isAlertSuccess = true;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  toggleRegister() {
    this.isRegister = !this.isRegister;
  }

  submit() {
    return this.isRegister ? this.register() : this.login();
  }

  private login() {
    this.authService.login({ email: this.user.email!, password: this.user.password! }).subscribe({
      next: (res: any) => {
        this.authService.setToken(res.token);
        this.authService.setRole(res.role);
        this.router.navigate([RouterConfig.HOME.link]);
      },
      error: (err) => {
        this.isAlertSuccess = false;
        this.alertMessage = MessageConstants.LOGIN_FAILED;
        this.isShowAlert = true;
        setTimeout(() => {
          this.isShowAlert = false;
        }, 3000);
      }
    });
  }

  private register() {
    this.authService.register({ name: this.user.name!, email: this.user.email!, password: this.user.password! }).subscribe({
      next: () => {
        this.isAlertSuccess = true;
        this.alertMessage = MessageConstants.REGISTER_SUCCESS;
        this.authForm.resetForm();
        this.isRegister = false;
        this.isShowAlert = true;
        setTimeout(() => {
          this.isShowAlert = false;
        }, 3000);
      },
      error: (err) => {
        this.isAlertSuccess = false;
        this.alertMessage = MessageConstants.REGIStER_FAILED(err);
        this.isShowAlert = true;
        setTimeout(() => {
          this.isShowAlert = false;
        }, 3000);
      }
    });
  };
}
