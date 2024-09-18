import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './login.component.html',
})
export class LoginComponent {
  isRegister = false;
  name = '';
  email = '';
  password = '';
  confirmPassword = '';

  toggleRegister() {
    this.isRegister = !this.isRegister;
  }
}
