import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DriverDataService, PersonalInfo } from '../driver-data.service';

@Component({
  selector: 'app-personal-info',
  imports: [FormsModule],
  template: `
    <h4 class="mb-3">Personal Information</h4>

    @if (saved()) { <div class="alert alert-success py-2">Saved successfully</div> }
    @if (message) { <div class="alert alert-danger py-2">{{ message }}</div> }

    <form #f="ngForm" (ngSubmit)="save()" class="card shadow-sm p-4" style="max-width:560px">
      <div class="mb-3">
        <label class="form-label">First Name</label>
        <input class="form-control" name="firstName" [(ngModel)]="info().firstName" required />
      </div>
      <div class="mb-3">
        <label class="form-label">Middle Name</label>
        <input class="form-control" name="middleName" [(ngModel)]="info().middleName" />
      </div>
      <div class="mb-3">
        <label class="form-label">Last Name</label>
        <input class="form-control" name="lastName" [(ngModel)]="info().lastName" required />
      </div>
      <div class="mb-3">
        <label class="form-label">Email</label>
        <input class="form-control" type="email" [value]="info().email" disabled />
      </div>
      <button class="btn btn-warning text-white fw-semibold" style="background:#f18700;border-color:#f18700"
              [disabled]="f.invalid || saving">{{ saving ? 'Saving…' : 'Save Changes' }}</button>
    </form>
  `
})
export class PersonalInfoComponent {
  private data = inject(DriverDataService);

  info = signal<PersonalInfo>({ uuid: '', firstName: '', lastName: '', email: '' });
  saved = signal(false);
  message = '';
  saving = false;

  ngOnInit(): void {
    this.data.personalInfo().subscribe({
      next: (p) => this.info.set(p),
      error: () => this.message = 'Failed to load profile'
    });
  }

  save(): void {
    if (this.saving) return;
    this.saving = true;
    this.saved.set(false);
    const cur = this.info();
    this.data.updatePersonalInfo({
      firstName: cur.firstName,
      middleName: cur.middleName ?? '',
      lastName: cur.lastName
    }).subscribe({
      next: (updated) => { this.info.set(updated); this.saved.set(true); this.saving = false; },
      error: () => { this.message = 'Failed to save'; this.saving = false; }
    });
  }
}
