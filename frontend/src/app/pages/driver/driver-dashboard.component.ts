import { Component, inject } from '@angular/core';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-driver-dashboard',
  template: `
    <h4>Welcome, {{ auth.user()?.firstName }}! 🚚</h4>
    <p class="text-muted">Manage your driver file from the menu on the left.</p>
    <div class="alert alert-info">
      More sections (employment history, accidents, documents…) are being migrated from
      the classic interface — coming soon.
    </div>
  `
})
export class DriverDashboardComponent {
  auth = inject(AuthService);
}
