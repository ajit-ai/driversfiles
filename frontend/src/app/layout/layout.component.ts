import { Component, inject } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-layout',
  imports: [RouterOutlet, RouterLink],
  template: `
    <nav class="navbar navbar-expand navbar-dark" style="background:linear-gradient(135deg,#2c3e50,#34495e);border-bottom:4px solid #f18700">
      <div class="container">
        <a class="navbar-brand fw-bold" routerLink="/">🚚 Drivers Files</a>
        <div class="navbar-nav me-auto"></div>
        <div class="d-flex align-items-center text-light gap-3">
          @if (auth.user(); as u) {
            <span class="small">{{ u.firstName }} {{ u.lastName }}
              <span class="badge bg-secondary ms-1">{{ u.type }}</span>
            </span>
            <button class="btn btn-sm btn-outline-light" (click)="logout()">Sign Out</button>
          }
        </div>
      </div>
    </nav>
    <div class="container py-4">
      <router-outlet />
    </div>
  `
})
export class LayoutComponent {
  auth = inject(AuthService);
  logout(): void {
    this.auth.logout();
  }
}
