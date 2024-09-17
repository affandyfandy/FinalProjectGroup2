import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { Router } from '@angular/router';
import { RouterConfig } from '../../config/app.constants';
import { FormsModule } from '@angular/forms';

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
  isRegister = false;
  name = '';
  email = '';
  password = '';
  confirmPassword = '';

  isShowSuccess = false;
  isShowError = false;
  alertMessage = '';

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
    this.authService.login({ email: this.email, password: this.password }).subscribe({
      next: (res: any) => {
        this.authService.setToken(res.token);
        this.authService.setRole(res.role);
        this.router.navigate([RouterConfig.HOME.link]);
      },
      error: (err) => {
        this.handleError(err);
      }
    });
  }

  private register() {
    this.authService.register({ name: this.name, email: this.email, password: this.password }).subscribe({
      next: () => {
        this.isShowSuccess = true;
        this.alertMessage = 'Register successfully';
        this.resetField();
      },
      error: (err) => {
        this.handleError(err);
      }
    });
  };

  private resetField() {
    this.name = '';
    this.email = '';
    this.password = '';
    this.confirmPassword = '';
    this.isRegister = false;
  }

  private handleError(err: any) {
    this.isShowError = true;
    this.alertMessage = err.error.message;
  }
}
