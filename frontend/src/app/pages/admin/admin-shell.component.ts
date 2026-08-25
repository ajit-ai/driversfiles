import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-admin-shell',
  imports: [RouterOutlet, RouterLink],
  template: `
    <div class="row g-4">
      <div class="col-md-3">
        <div class="list-group shadow-sm">
          <a routerLink="/admin" routerLinkActive="active" class="list-group-item list-group-item-action">Dashboard</a>
          <a routerLink="/admin/users" routerLinkActive="active" class="list-group-item list-group-item-action">Users</a>
          <a routerLink="/admin/imports" routerLinkActive="active" class="list-group-item list-group-item-action">Data Imports</a>
          <a routerLink="/admin/content" routerLinkActive="active" class="list-group-item list-group-item-action">CMS Content</a>
        </div>
      </div>
      <div class="col-md-9"><router-outlet /></div>
    </div>
  `
})
export class AdminShellComponent {}
