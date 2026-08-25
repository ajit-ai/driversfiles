import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-company-shell',
  imports: [RouterOutlet, RouterLink],
  template: `
    <div class="row g-4">
      <div class="col-md-3">
        <div class="list-group shadow-sm">
          <a routerLink="/company" routerLinkActive="active" class="list-group-item list-group-item-action">Dashboard</a>
          <a routerLink="/company/profile" routerLinkActive="active" class="list-group-item list-group-item-action">My Company</a>
          <a routerLink="/company/drivers" routerLinkActive="active" class="list-group-item list-group-item-action">Drivers</a>
        </div>
      </div>
      <div class="col-md-9"><router-outlet /></div>
    </div>
  `
})
export class CompanyShellComponent {}