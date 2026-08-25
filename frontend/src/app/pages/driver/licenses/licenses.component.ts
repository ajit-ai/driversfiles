import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { DriverDataService } from '../driver-data.service';
import { License } from '../driver.models';

@Component({
  selector: 'app-licenses',
  imports: [FormsModule, DatePipe],
  template: `
    <h4 class="mb-3">CDL / Licenses</h4>

    @if (message) { <div class="alert alert-danger py-2">{{ message }}</div> }

    <table class="table table-hover align-middle bg-white shadow-sm rounded">
      <thead class="table-dark">
        <tr><th>State</th><th>Number</th><th>Type</th><th>Expiration</th><th>Current</th><th></th></tr>
      </thead>
      <tbody>
        @for (l of list(); track l.uuid) {
          <tr>
            <td>{{ l.state }}</td>
            <td>{{ l.number }}</td>
            <td>{{ l.type }}</td>
            <td>{{ l.expiration ? (l.expiration | date: 'MMM d, y') : '—' }}</td>
            <td>@if (l.current) { <span class="badge text-bg-success">Current</span> }</td>
            <td class="text-end">
              <button class="btn btn-sm btn-outline-danger" (click)="remove(l)">Delete</button>
            </td>
          </tr>
        } @empty {
          <tr><td colspan="6" class="text-center text-muted py-4">No licenses recorded</td></tr>
        }
      </tbody>
    </table>

    <div class="card mt-3 shadow-sm">
      <div class="card-body">
        <h6 class="card-title">Add License</h6>
        <form #f="ngForm" (ngSubmit)="add()" class="row g-2 align-items-end">
          <div class="col-auto"><input class="form-control" name="state" [(ngModel)]="form.state"
                 placeholder="State (e.g. TX)" required maxlength="2" size="4" /></div>
          <div class="col-auto"><input class="form-control" name="number" [(ngModel)]="form.number"
                 placeholder="License number" required /></div>
          <div class="col-auto">
            <select class="form-select" name="type" [(ngModel)]="form.type" required>
              <option [ngValue]="undefined" disabled>Type…</option>
              @for (t of types; track t) { <option [value]="t">{{ t }}</option> }
            </select>
          </div>
          <div class="col-auto"><input class="form-control" type="date" name="expiration"
                 [(ngModel)]="form.expiration" /></div>
          <div class="col-auto form-check ms-1">
            <input class="form-check-input" type="checkbox" name="current"
                   [(ngModel)]="form.current" id="cur" />
            <label class="form-check-label" for="cur">Current</label>
          </div>
          <div class="col-auto">
            <button class="btn btn-warning text-white fw-semibold" style="background:#f18700;border-color:#f18700"
                    [disabled]="f.invalid || saving">{{ saving ? 'Saving…' : 'Add' }}</button>
          </div>
        </form>
      </div>
    </div>
  `
})
export class LicensesComponent {
  private data = inject(DriverDataService);

  readonly types = ['CLASSA','CLASSB','CLASSC','CLASSD','CLASSDJ','CLASSE','CLASSMJ'];
  list = signal<License[]>([]);
  message = '';
  saving = false;
  form = { state: '', number: '', type: undefined as string | undefined,
           expiration: '' as string | undefined, current: true };

  ngOnInit(): void { this.load(); }

  load(): void {
    this.data.licenses().subscribe({
      next: (rows) => this.list.set(rows),
      error: () => this.message = 'Failed to load licenses'
    });
  }

  add(): void {
    if (this.saving) return;
    this.saving = true;
    this.message = '';
    const body = {
      state: this.form.state.toUpperCase(),
      number: this.form.number,
      type: this.form.type!,
      expiration: this.form.expiration || undefined,
      current: this.form.current
    };
    this.data.addLicense(body).subscribe({
      next: () => { this.saving = false; this.reset(); this.load(); },
      error: () => { this.message = 'Failed to save license'; this.saving = false; }
    });
  }

  remove(l: License): void {
    if (!confirm('Delete license ' + l.number + '?')) return;
    this.data.deleteLicense(l.uuid).subscribe({ next: () => this.load() });
  }

  reset(): void {
    this.form = { state: '', number: '', type: undefined, expiration: '', current: true };
  }
}
