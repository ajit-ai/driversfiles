import { Component } from '@angular/core';

@Component({
  selector: 'app-coming-soon',
  template: `
    <div class="alert alert-info shadow-sm">
      <strong>Coming soon.</strong> This portal is being migrated from the classic interface.
      Use the classic UI at <a href="http://localhost:8080">localhost:8080</a> in the meantime.
    </div>
  `
})
export class ComingSoonComponent {}
