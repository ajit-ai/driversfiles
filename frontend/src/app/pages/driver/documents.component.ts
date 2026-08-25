import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { API_BASE, AuthService } from '../../core/auth.service';
import { DriverDataService } from './driver-data.service';
import { DriverDocument } from './driver.models';

const DOC_TYPES = [
  { code: 'DOC_TYPE_CDL', label: 'CDL' },
  { code: 'DOC_TYPE_MED_CARD', label: 'Medical Card' },
  { code: 'DOC_TYPE_PHYSICAL', label: 'Long Form Physical' },
  { code: 'DOC_TYPE_SS_CARD', label: 'Social Security Card' }
];

@Component({
  selector: 'app-documents',
  imports: [FormsModule, DatePipe],
  template: `
    <h4 class="mb-3">Documents</h4>
    @if (message) { <div class="alert alert-danger py-2">{{ message }}</div> }

    <table class="table table-hover align-middle bg-white shadow-sm rounded">
      <thead class="table-dark">
        <tr><th>Type</th><th>File</th><th>Expiration</th><th class="text-end">Actions</th></tr>
      </thead>
      <tbody>
        @for (d of docs(); track d.uuid) {
          <tr>
            <td>{{ label(d.typeCode) }}</td>
            <td>{{ d.filename }}</td>
            <td>{{ d.expirationDate ? (d.expirationDate | date: 'MMM d, y') : '—' }}</td>
            <td class="text-end">
              <button class="btn btn-sm btn-outline-primary me-1" (click)="download(d)">Download</button>
              <button class="btn btn-sm btn-outline-danger" (click)="remove(d)">Delete</button>
            </td>
          </tr>
        } @empty {
          <tr><td colspan="4" class="text-center text-muted py-4">No documents uploaded</td></tr>
        }
      </tbody>
    </table>

    <div class="card mt-3 shadow-sm"><div class="card-body">
      <h6 class="card-title">Upload Document</h6>
      <form class="row g-2 align-items-end">
        <div class="col-auto">
          <select class="form-select" name="typeCode" [(ngModel)]="typeCode" required>
            <option [ngValue]="undefined" disabled>Type…</option>
            @for (t of docTypes; track t.code) { <option [value]="t.code">{{ t.label }}</option> }
          </select>
        </div>
        <div class="col-auto"><input class="form-control" type="date" name="exp"
               [(ngModel)]="expiration" placeholder="Expiration (CDL required)" /></div>
        <div class="col-auto">
          <input class="form-control" type="file" accept=".pdf,.jpg,.jpeg,.png"
                 (change)="onFile($event)" #fileInput />
        </div>
        <div class="col-auto">
          <button class="btn btn-warning text-white fw-semibold"
                  style="background:#f18700;border-color:#f18700"
                  [disabled]="!selected || !typeCode || uploading" (click)="upload()">
            {{ uploading ? 'Uploading…' : 'Upload' }}
          </button>
        </div>
      </form>
    </div></div>
  `
})
export class DocumentsComponent {
  private data = inject(DriverDataService);
  private http = inject(HttpClient);
  private auth = inject(AuthService);

  readonly docTypes = DOC_TYPES;
  docs = signal<DriverDocument[]>([]);
  message = '';
  typeCode?: string;
  expiration = '';
  selected: File | null = null;
  uploading = false;

  ngOnInit(): void { this.load(); }

  label(code: string): string {
    return this.docTypes.find((t) => t.code === code)?.label ?? code;
  }

  load(): void {
    this.data.documents().subscribe({ next: (d) => this.docs.set(d), error: () => this.message = 'Failed to load documents' });
  }

  onFile(e: Event): void {
    const input = e.target as HTMLInputElement;
    this.selected = input.files && input.files.length ? input.files[0] : null;
  }

  upload(): void {
    if (!this.selected || !this.typeCode || this.uploading) return;
    const form = new FormData();
    form.append('file', this.selected);
    form.append('typeCode', this.typeCode);
    if (this.expiration) form.append('expirationDate', this.expiration);
    this.uploading = true;
    this.data.uploadDocument(form).subscribe({
      next: () => { this.uploading = false; this.selected = null; this.load(); },
      error: (e) => { this.message = e.error?.error ?? 'Upload failed'; this.uploading = false; }
    });
  }

  download(d: DriverDocument): void {
    const headers = new HttpHeaders({ Authorization: `Bearer ${this.auth.token()}` });
    this.http.get(`${API_BASE}/driver/me/documents/${d.uuid}/download`,
      { headers, responseType: 'blob' }).subscribe((blob) => {
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url; a.download = d.filename; a.click();
      URL.revokeObjectURL(url);
    });
  }

  remove(d: DriverDocument): void {
    if (!confirm('Delete ' + this.label(d.typeCode) + '?')) return;
    this.data.deleteDocument(d.uuid).subscribe({ next: () => this.load() });
  }
}
