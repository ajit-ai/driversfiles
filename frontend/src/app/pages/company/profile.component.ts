import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CompanyDataService, CompanyProfile } from './company-data.service';

@Component({
  selector: 'app-company-profile',
  imports: [FormsModule],
  template: `
    <h4 class="mb-3">Company Profile</h4>
    @if (saved()) { <div class="alert alert-success py-2">Saved successfully</div> }
    <form #f="ngForm" (ngSubmit)="save()" class="card shadow-sm p-4" style="max-width:640px">
      <div class="row">
        <div class="col-md-6 mb-3"><label class="form-label">Company Name</label>
          <input class="form-control" name="name" [(ngModel)]="info().name" required /></div>
        <div class="col-md-6 mb-3"><label class="form-label">Company Number</label>
          <input class="form-control" [value]="info().companyNumber" disabled /></div>
        <div class="col-md-6 mb-3"><label class="form-label">Address 1</label>
          <input class="form-control" name="address1" [(ngModel)]="info().address1" /></div>
        <div class="col-md-6 mb-3"><label class="form-label">Address 2</label>
          <input class="form-control" name="address2" [(ngModel)]="info().address2" /></div>
        <div class="col-md-4 mb-3"><label class="form-label">City</label>
          <input class="form-control" name="city" [(ngModel)]="info().city" /></div>
        <div class="col-md-4 mb-3"><label class="form-label">State</label>
          <input class="form-control" name="state" [(ngModel)]="info().state" maxlength="2" /></div>
        <div class="col-md-4 mb-3"><label class="form-label">Postal Code</label>
          <input class="form-control" name="postalCode" [(ngModel)]="info().postalCode" /></div>
        <div class="col-md-4 mb-3"><label class="form-label">Phone</label>
          <input class="form-control" name="phone" [(ngModel)]="info().phone" /></div>
        <div class="col-md-4 mb-3"><label class="form-label">Fax</label>
          <input class="form-control" name="fax" [(ngModel)]="info().fax" /></div>
        <div class="col-md-4 mb-3"><label class="form-label">Website</label>
          <input class="form-control" name="website" [(ngModel)]="info().website" /></div>
      </div>
      <button class="btn btn-warning text-white fw-semibold px-4"
              style="background:#f18700;border-color:#f18700"
              [disabled]="f.invalid || saving()">{{ saving() ? 'Saving…' : 'Save Changes' }}</button>
    </form>
  `
})
export class CompanyProfileComponent {
  private data = inject(CompanyDataService);
  info = signal<CompanyProfile>({ uuid: '', name: '' });
  saved = signal(false);
  saving = signal(false);

  ngOnInit(): void {
    this.data.profile().subscribe((p) => this.info.set(p));
  }

  save(): void {
    this.saving.set(true);
    const cur = this.info();
    this.data.updateProfile(cur).subscribe({
      next: (updated) => { this.info.set(updated); this.saved.set(true); this.saving.set(false); },
      error: () => this.saving.set(false)
    });
  }
}