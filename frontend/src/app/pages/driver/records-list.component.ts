import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DriverDataService } from './driver-data.service';

export const RECORDS_TEMPLATE = `
  <h4 class="mb-3">{{ title }}</h4>
  @if (message) { <div class="alert alert-danger py-2">{{ message }}</div> }

  <table class="table table-hover align-middle bg-white shadow-sm rounded">
    <thead class="table-dark">
      <tr>@for (c of columns; track c) { <th>{{ c }}</th> } <th></th></tr>
    </thead>
    <tbody>
      @for (row of rows(); track $index) {
        <tr>@for (f of fields; track f.name) { <td>{{ display(row, f.name) }}</td> }
          <td class="text-end">
            <button class="btn btn-sm btn-outline-danger" (click)="remove(row)">Delete</button>
          </td>
        </tr>
      } @empty {
        <tr><td [attr.colspan]="columns.length + 1" class="text-center text-muted py-4">No records</td></tr>
      }
    </tbody>
  </table>

  <div class="card mt-3 shadow-sm"><div class="card-body">
    <h6 class="card-title">Add {{ singular }}</h6>
    <form #f="ngForm" (ngSubmit)="add()" class="row g-2 align-items-end">
      @for (fld of fields; track fld.name) {
        <div class="col-auto" [class.form-check]="fld.type === 'checkbox'">
          @if (fld.type === 'checkbox') {
            <input class="form-check-input" type="checkbox" [name]="fld.name"
                   [(ngModel)]="form[fld.name]" [id]="fld.name" />
            <label class="form-check-label" [for]="fld.name">{{ fld.label }}</label>
          } @else {
            <input class="form-control" [type]="fld.type === 'date' ? 'date' : 'text'" [name]="fld.name"
                   [(ngModel)]="form[fld.name]" [placeholder]="fld.label"
                   [required]="fld.required ?? false" />
          }
        </div>
      }
      <div class="col-auto">
        <button class="btn btn-warning text-white fw-semibold"
                style="background:#f18700;border-color:#f18700"
                [disabled]="f.invalid || saving">{{ saving ? 'Saving…' : 'Add' }}</button>
      </div>
    </form>
  </div></div>
`;

export interface FieldDef {
  name: string;
  label: string;
  type?: 'text' | 'date' | 'checkbox';
  required?: boolean;
}

@Component({
  selector: 'app-records-list',
  imports: [FormsModule],
  template: ''
})
export abstract class RecordsListComponent<T extends { uuid?: string }> {
  protected data = inject(DriverDataService);

  abstract title: string;
  abstract singular: string;
  abstract columns: string[];
  abstract fields: FieldDef[];
  protected abstract loadRows(): import('rxjs').Observable<T[]>;
  protected abstract addRow(form: any): import('rxjs').Observable<T>;
  protected abstract removeRow(row: T): import('rxjs').Observable<void>;

  rows = signal<T[]>([]);
  message = '';
  saving = false;
  form: Record<string, any> = {};

  ngOnInit(): void {
    this.load();
    this.reset();
  }

  load(): void {
    this.loadRows().subscribe({ next: (r) => this.rows.set(r), error: () => this.message = 'Failed to load' });
  }

  display(row: T, field: string): string {
    const v = (row as any)[field];
    if (v == null || v === '') return '—';
    if (/Date$/.test(field)) return String(v).substring(0, 10);
    return String(v);
  }

  reset(): void {
    this.form = {};
    for (const f of this.fields) {
      this.form[f.name] = f.type === 'checkbox' ? false : '';
    }
  }

  add(): void {
    if (this.saving) return;
    this.saving = true;
    this.message = '';
    this.addRow(this.form).subscribe({
      next: () => { this.saving = false; this.reset(); this.load(); },
      error: () => { this.message = 'Failed to save'; this.saving = false; }
    });
  }

  remove(row: T): void {
    if (!confirm('Delete this record?')) return;
    this.removeRow(row).subscribe({ next: () => this.load(), error: () => this.message = 'Delete failed' });
  }
}
