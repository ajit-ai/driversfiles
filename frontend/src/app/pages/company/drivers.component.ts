import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { CompanyDataService, GrantedDriver } from './company-data.service';

@Component({
  selector: 'app-company-drivers',
  imports: [DatePipe],
  template: `
    <h4 class="mb-3">Driver Files Shared With Us</h4>
    <table class="table table-hover align-middle bg-white shadow-sm rounded">
      <thead class="table-dark"><tr><th>Driver</th><th>Email</th><th>Granted On</th></tr></thead>
      <tbody>
        @for (d of list(); track d.email + d.grantedDate) {
          <tr><td>{{ d.name }}</td><td>{{ d.email }}</td>
            <td>{{ d.grantedDate ? (d.grantedDate | date: 'MMM d, y') : '—' }}</td></tr>
        } @empty {
          <tr><td colspan="3" class="text-center text-muted py-4">
            No driver files shared yet.</td></tr>
        }
      </tbody>
    </table>
  `
})
export class CompanyDriversComponent {
  private data = inject(CompanyDataService);
  list = signal<GrantedDriver[]>([]);
  ngOnInit(): void {
    this.data.drivers().subscribe({ next: (d) => this.list.set(d) });
  }
}