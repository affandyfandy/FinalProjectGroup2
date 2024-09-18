import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './header.component.html'
})
export class HeaderComponent {
  isLoggedIn = false;

  constructor(private router: Router) { }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
