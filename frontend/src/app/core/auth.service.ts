import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { LoginResponse, UserInfo, isTokenExpired } from './models';

const TOKEN_KEY = 'df_token';
const USER_KEY = 'df_user';
export const API_BASE = location.port === '4200' ? 'http://localhost:8080/api' : '/api';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  readonly user = signal<UserInfo | null>(this.restoreUser());
  readonly isLoggedIn = computed(() => {
    const t = this.token();
    return !!t && !isTokenExpired(t);
  });
  readonly token = signal<string | null>(this.restoreToken());

  login(email: string, password: string) {
    return this.http.post<LoginResponse>(`${API_BASE}/auth/login`, { email, password });
  }

  handleLoginSuccess(res: LoginResponse) {
    localStorage.setItem(TOKEN_KEY, res.token);
    localStorage.setItem(USER_KEY, JSON.stringify(res.user));
    this.token.set(res.token);
    this.user.set(res.user);
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this.token.set(null);
    this.user.set(null);
    this.router.navigate(['/login']);
  }

  private restoreToken(): string | null {
    const t = localStorage.getItem(TOKEN_KEY);
    return t && !isTokenExpired(t) ? t : null;
  }

  private restoreUser(): UserInfo | null {
    try {
      const raw = localStorage.getItem(USER_KEY);
      const parsed = raw ? (JSON.parse(raw) as UserInfo) : null;
      return this.restoreToken() ? parsed : null;
    } catch {
      return null;
    }
  }
}
