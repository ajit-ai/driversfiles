import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminDataService, ImportRow } from './admin-data.service';

@Component({
  selector: 'app-admin-imports',
  imports: [FormsModule, DatePipe],
  template: `
    <h4 class="mb-3">Data Imports</h4>
    @if (message) { <div class="alert py-2" [class.alert-success]="!isError"
      [class.alert-danger]="isError">{{ message }}</div> }

    <div class="card mb-4 shadow-sm"><div class="card-body">
      <h6 class="card-title">New Import</h6>
      <form class="row g-2 align-items-end">
        <div class="col-auto">
          <select class="form-select form-select-sm" name="importType" [(ngModel)]="type" required>
            <option [ngValue]="undefined" disabled>Import type…</option>
            @for (t of meta().types; track t) { <option [value]="t">{{ t }}</option> }
          </select>
        </div>
        <div class="col-auto">
          <select class="form-select form-select-sm" name="companyId" [(ngModel)]="companyId" required>
            <option [ngValue]="undefined" disabled>Company…</option>
            @for (c of meta().companies; track c.id) { <option [value]="c.id">{{ c.name }}</option> }
          </select>
        </div>
        <div class="col-auto form-check pb-1">
          <input class="form-check-input" type="checkbox" name="overwrite" [(ngModel)]="overwrite" id="ow" />
          <label class="form-check-label small" for="ow">Overwrite existing</label>
        </div>
        <div class="col-auto">
          <input class="form-control form-control-sm" type="file" accept=".csv"
                 (change)="onFile($event)" #fileInput />
        </div>
        <div class="col-auto">
          <button class="btn btn-sm btn-warning text-white fw-semibold"
                  style="background:#f18700;border-color:#f18700"
                  [disabled]="!selected || !type || !companyId || uploading" (click)="upload()">
            {{ uploading ? 'Uploading…' : 'Upload & Queue' }}
          </button>
        </div>
      </form>
    </div></div>

    <table class="table table-hover align-middle bg-white shadow-sm rounded">
      <thead class="table-dark"><tr>
        <th>#</th><th>Type</th><th>Company</th><th>Status</th><th>Created</th><th></th></tr></thead>
      <tbody>
        @for (i of list(); track i.id) {
          <tr>
            <td>{{ i.id }}</td><td>{{ i.importType }}</td><td>{{ i.companyName }}</td>
            <td>@if (i.success === true) { <span class="badge text-bg-success">Success</span> }
                @else if (i.success === false) { <span class="badge text-bg-danger">Failed</span> }
                @else { <span class="badge text-bg-secondary">Pending</span> }</td>
            <td>{{ i.createdDate ? (i.createdDate | date: 'MMM d, y h:mm a') : '—' }}</td>
            <td class="text-end">
              <button class="btn btn-sm btn-outline-danger" (click)="remove(i)">Delete</button></td>
          </tr>
        } @empty {
          <tr><td colspan="6" class="text-center text-muted py-4">No imports</td></tr>
        }
      </tbody>
    </table>
  `
})
export class AdminImportsComponent {
  private data = inject(AdminDataService);

  readonly types = ['DRIVERS'];
  meta = signal<{ types: string[]; companies: { id: number; name: string }[] }>({ types: [], companies: [] });
  list = signal<ImportRow[]>([]);
  message = '';
  isError = false;
  type?: string;
  companyId?: number;
  overwrite = false;
  selected: File | null = null;
  uploading = false;

  ngOnInit(): void {
    this.data.importsMeta().subscribe((m) => this.meta.set(m));
    this.load();
  }

  onFile(e: Event): void {
    const input = e.target as HTMLInputElement;
    this.selected = input.files && input.files.length ? input.files[0] : null;
  }

  load(): void {
    this.data.imports().subscribe({ next: (rows) => this.list.set(rows) });
  }

  upload(): void {
    if (!this.selected || this.uploading) return;
    const form = new FormData();
    form.append('file', this.selected);
    form.append('importType', this.type!);
    form.append('companyId', String(this.companyId));
    form.append('overwrite', String(this.overwrite));
    this.uploading = true;
    this.data.createImport(form).subscribe({
      next: () => { this.uploading = false; this.message = 'Import queued — processed by background job'; this.isError = false; this.selected = null; this.load(); },
      error: () => { this.message = 'Upload failed'; this.isError = true; this.uploading = false; }
    });
  }

  remove(i: ImportRow): void {
    if (!confirm('Delete import #' + i.id + '?')) return;
    this.data.deleteImport(i.id).subscribe({ next: () => this.load() });
  }
}
