import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  template: `
    <div class="login-wrap">
      <div class="card login-card shadow">
        <div class="card-body p-4">
          <h3 class="text-center mb-1">Drivers Files</h3>
          <p class="text-center text-muted mb-4">Sign in to your account</p>

          @if (error) {
            <div class="alert alert-danger py-2">{{ error }}</div>
          }

          <form (ngSubmit)="submit()">
            <div class="mb-3">
              <label class="form-label">Email</label>
              <input class="form-control" type="email" name="email"
                     [(ngModel)]="email" required autocomplete="username" />
            </div>
            <div class="mb-4">
              <label class="form-label">Password</label>
              <input class="form-control" type="password" name="password"
                     [(ngModel)]="password" required autocomplete="current-password" />
            </div>
            <button class="btn btn-warning w-100 fw-semibold" style="background:#f18700;border-color:#f18700;color:#fff"
                    [disabled]="loading">
              {{ loading ? 'Signing in…' : 'Sign In' }}
            </button>
          </form>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .login-wrap { min-height: calc(100vh - 60px); display: flex; align-items: center; justify-content: center; }
    .login-card { width: 380px; border-radius: 14px; }
  `]
})
export class LoginComponent {
  private auth = inject(AuthService);
  private router = inject(Router);

  email = '';
  password = '';
  error = '';
  loading = false;

  submit(): void {
    if (!this.email || !this.password || this.loading) return;
    this.loading = true;
    this.error = '';
    this.auth.login(this.email.trim(), this.password).subscribe({
      next: (res) => {
        this.auth.handleLoginSuccess(res);
        const roles = res.user.roles ?? [];
        if (roles.includes('ROLE_ADMIN')) this.router.navigate(['/admin']);
        else if (roles.includes('ROLE_COMPANY')) this.router.navigate(['/company']);
        else this.router.navigate(['/driver']);
      },
      error: () => {
        this.error = 'Invalid email or password';
        this.loading = false;
      }
    });
  }
}
