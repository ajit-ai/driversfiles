import { Component, inject } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-driver-shell',
  imports: [RouterOutlet, RouterLink],
  template: `
    <div class="row g-4">
      <div class="col-md-3">
        <div class="list-group shadow-sm">
          <a routerLink="/driver" routerLinkActive="active" class="list-group-item list-group-item-action">Dashboard</a>
          <a routerLink="/driver/personal-info" routerLinkActive="active" class="list-group-item list-group-item-action">Personal Information</a>
          <a routerLink="/driver/licenses" routerLinkActive="active" class="list-group-item list-group-item-action">CDL / Licenses</a>
        </div>
      </div>
      <div class="col-md-9"><router-outlet /></div>
    </div>
  `
})
export class DriverShellComponent {
  auth = inject(AuthService);
}
