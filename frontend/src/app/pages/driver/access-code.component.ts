import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { DriverDataService } from './driver-data.service';

@Component({
  selector: 'app-access-code',
  imports: [DatePipe],
  template: `
    <h4 class="mb-3">My Access Code</h4>
    <div class="card shadow-sm p-4 text-center" style="max-width:480px">
      @if (code(); as c) {
        <div class="display-4 fw-bold" style="color:#f18700;letter-spacing:.15em">{{ c }}</div>
        @if (expire(); as e) { <p class="text-muted mt-2 mb-0">Expires {{ e | date: 'MMM d, y h:mm a' }}</p> }
      } @else {
        <p class="text-muted">No active access code.</p>
      }
      <button class="btn btn-warning text-white fw-semibold mt-3 mx-auto px-4"
              style="background:#f18700;border-color:#f18700" (click)="generate()"
              [disabled]="loading">
        {{ loading ? 'Generating…' : (code() ? 'Regenerate Code' : 'Generate Code') }}
      </button>
      <p class="small text-muted mt-3 mb-0">Share this code with a company so they can request your driver file.</p>
    </div>
  `
})
export class AccessCodeComponent {
  private data = inject(DriverDataService);
  code = signal<string | null>(null);
  expire = signal<string | null>(null);
  loading = false;

  ngOnInit(): void { this.generate(); }

  generate(): void {
    if (this.loading) return;
    this.loading = true;
    this.data.accessCode().subscribe({
      next: (info) => {
        this.code.set(info.accessCode);
        this.expire.set(info.createdDate ?? null);
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }
}