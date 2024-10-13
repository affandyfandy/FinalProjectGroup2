import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-unauthorized',
  standalone: true,
  imports: [],
  templateUrl: './unauthorized.component.html'
})
export class UnauthorizedComponent {

  constructor(private router: Router) { }

  goHome() {
    this.router.navigate(['/']);
  }
}
