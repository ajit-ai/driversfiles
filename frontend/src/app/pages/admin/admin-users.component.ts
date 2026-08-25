import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminDataService, AdminUser } from './admin-data.service';

@Component({
  selector: 'app-admin-users',
  imports: [FormsModule],
  template: `
    <h4 class="mb-3">
      User Management
      <button class="btn btn-warning text-white float-end" style="background:#f18700;border-color:#f18700"
              (click)="openNew()">New User</button>
    </h4>

    <form class="row g-2 align-items-end mb-3" (ngSubmit)="search()">
      <div class="col-auto"><input class="form-control form-control-sm" name="firstName"
             [(ngModel)]="filters.firstName" placeholder="First name" /></div>
      <div class="col-auto"><input class="form-control form-control-sm" name="lastName"
             [(ngModel)]="filters.lastName" placeholder="Last name" /></div>
      <div class="col-auto"><input class="form-control form-control-sm" name="email"
             [(ngModel)]="filters.email" placeholder="Email" /></div>
      <div class="col-auto">
        <select class="form-select form-select-sm" name="type" [(ngModel)]="filters.type">
          <option value="">All types</option>
          @for (t of types; track t) { <option [value]="t">{{ t }}</option> }
        </select>
      </div>
      <div class="col-auto"><button class="btn btn-sm btn-primary">Search</button></div>
    </form>

    @if (message) { <div class="alert py-2"
      [class.alert-success]="!isError" [class.alert-danger]="isError">{{ message }}</div> }

    <table class="table table-hover align-middle bg-white shadow-sm rounded">
      <thead class="table-dark"><tr>
        <th>First</th><th>Last</th><th>Email</th><th>Type</th><th>Company</th><th></th>
      </tr></thead>
      <tbody>
        @for (u of users(); track u.id) {
          <tr>
            <td>{{ u.firstName }}</td><td>{{ u.lastName }}</td><td>{{ u.email }}</td>
            <td><span class="badge text-bg-secondary">{{ u.type }}</span></td>
            <td>{{ u.companyName || '—' }}</td>
            <td class="text-end">
              <button class="btn btn-sm btn-outline-primary me-1" (click)="edit(u)">Edit</button>
              <button class="btn btn-sm btn-outline-danger" (click)="remove(u)">Delete</button>
            </td>
          </tr>
        } @empty {
          <tr><td colspan="6" class="text-center text-muted py-4">No users found</td></tr>
        }
      </tbody>
    </table>

    @if (editing) {
      <div class="card mt-3 shadow-sm"><div class="card-body">
        <h6>{{ form.id ? 'Edit User #' + form.id : 'New User' }}</h6>
        <form #f="ngForm" (ngSubmit)="save()" class="row g-2">
          <div class="col-md-4"><input class="form-control form-control-sm" name="firstName"
                 [(ngModel)]="form.firstName" placeholder="First name *" required /></div>
          <div class="col-md-4"><input class="form-control form-control-sm" name="lastName"
                 [(ngModel)]="form.lastName" placeholder="Last name *" required /></div>
          <div class="col-md-4"><input class="form-control form-control-sm" type="email" name="email"
                 [(ngModel)]="form.email" placeholder="Email *" required /></div>
          <div class="col-md-4">
            <select class="form-select form-select-sm" name="type" [(ngModel)]="form.type" required>
              <option [ngValue]="undefined" disabled>Type…</option>
              @for (t of types; track t) { <option [value]="t">{{ t }}</option> }
            </select>
          </div>
          <div class="col-md-4"><input class="form-control form-control-sm" type="password" name="password"
                 [(ngModel)]="form.password"
                 [placeholder]="form.id ? 'New password (blank = keep)' : 'Password *'" />
          </div>
          @if (form.type === 'COMPANY') {
            <div class="col-md-4"><input class="form-control form-control-sm" name="companyName"
                   [(ngModel)]="form.companyName" placeholder="Company name" /></div>
            <div class="col-md-4"><input class="form-control form-control-sm" name="companyNumber"
                   [(ngModel)]="form.companyNumber" placeholder="Company number" /></div>
          }
          <div class="col-12">
            <button class="btn btn-sm btn-warning text-white fw-semibold"
                    style="background:#f18700;border-color:#f18700"
                    [disabled]="f.invalid || saving()">
              {{ saving() ? 'Saving…' : 'Save' }}</button>
            <button type="button" class="btn btn-sm btn-secondary ms-1" (click)="close()">Cancel</button>
          </div>
        </form>
      </div></div>
    }
  `
})
export class AdminUsersComponent {
  private data = inject(AdminDataService);
  private router = inject(Router);

  readonly types = ['ADMIN', 'COMPANY', 'DRIVER'];
  users = signal<AdminUser[]>([]);
  message = '';
  isError = false;
  saving = signal(false);
  editing = false;
  filters = { firstName: '', lastName: '', email: '', type: '', companyName: '', companyNumber: '', max: -1 };
  form: any = {};

  ngOnInit(): void { this.search(); }

  search(): void {
    this.data.users(this.filters).subscribe({
      next: (res) => this.users.set(res.users),
      error: () => this.flash('Search failed', true)
    });
  }

  openNew(): void {
    this.form = { firstName: '', lastName: '', email: '', password: '', type: undefined,
                  companyName: '', companyNumber: '' };
    this.editing = true;
  }

  edit(u: AdminUser): void {
    this.router.navigate([], { queryParams: { edit: u.id }, queryParamsHandling: 'merge' });
    this.form = { id: u.id, firstName: u.firstName, lastName: u.lastName, email: u.email,
                  password: '', type: u.type, companyName: u.companyName ?? '',
                  companyNumber: u.companyNumber ?? '' };
    this.editing = true;
  }

  close(): void { this.editing = false; }

  save(): void {
    if (this.saving()) return;
    if ((this.form.type === 'COMPANY') && (!this.form.companyName || !this.form.companyNumber)) {
      this.flash('Company accounts need company name and number', true);
      return;
    }
    if (!this.form.id && !this.form.password) {
      this.flash('Password is required for new users', true);
      return;
    }
    this.saving.set(true);
    this.data.saveUser(this.form).subscribe({
      next: () => { this.saving.set(false); this.editing = false; this.flash('Saved', false); this.search(); },
      error: (e) => {
        this.saving.set(false);
        this.flash(e.error?.error ?? e.error ?? 'Save failed — check for duplicate email/company', true);
      }
    });
  }

  remove(u: AdminUser): void {
    if (!confirm('Delete user ' + u.email + '?')) return;
    this.data.deleteUser(u.id).subscribe({
      next: () => this.search(),
      error: () => this.flash('Delete failed', true)
    });
  }

  flash(msg: string, isError: boolean): void {
    this.message = msg;
    this.isError = isError;
  }
}
