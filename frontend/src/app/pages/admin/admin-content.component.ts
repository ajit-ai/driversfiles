import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AdminDataService } from './admin-data.service';

@Component({
  selector: 'app-admin-content',
  imports: [FormsModule],
  template: `
    <h4 class="mb-3">CMS Content</h4>
    @if (saved()) { <div class="alert alert-success py-2">Content saved</div> }

    <div class="mb-3" style="max-width:320px">
      <label class="form-label">Page section</label>
      <select class="form-select" [ngModel]="selected()" (ngModelChange)="pick($event)">
        @for (n of names(); track n.name) { <option [value]="n.name">{{ n.name }}</option> }
      </select>
    </div>

    @if (loaded()) {
      <textarea class="form-control shadow-sm" rows="12" [(ngModel)]="content"></textarea>
      <button class="btn btn-warning text-white fw-semibold mt-3 px-4"
              style="background:#f18700;border-color:#f18700"
              [disabled]="saving()">{{ saving() ? 'Saving…' : 'Save Content' }}</button>
    }
  `
})
export class AdminContentComponent {
  private data = inject(AdminDataService);

  names = signal<{ name: string }[]>([]);
  selected = signal<string>('HOME');
  content = signal('');
  loaded = signal(false);
  saved = signal(false);
  saving = signal(false);

  ngOnInit(): void {
    this.data.contentNames().subscribe((names) => {
      this.names.set(names);
      if (names.length) this.pick(names[0].name);
    });
  }

  pick(name: string): void {
    this.selected.set(name);
    this.loaded.set(false);
    this.saved.set(false);
    this.data.contentNode(name).subscribe((node) => {
      this.content.set(node.content);
      this.loaded.set(true);
    });
  }

  save(): void {
    if (this.saving()) return;
    this.saving.set(true);
    this.data.saveContent(this.selected(), this.content()).subscribe({
      next: () => { this.saving.set(false); this.saved.set(true); },
      error: () => { this.saving.set(false); }
    });
  }
}
