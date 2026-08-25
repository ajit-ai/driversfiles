import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  if (auth.isLoggedIn()) return true;
  inject(Router).navigate(['/login']);
  return false;
};

export function roleGuard(...allowed: string[]): CanActivateFn {
  return () => {
    const auth = inject(AuthService);
    const ok =
      auth.isLoggedIn() &&
      (auth.user()?.roles ?? []).some((r) => allowed.some((a) => r === a || r.endsWith(a)));
    if (!ok) inject(Router).navigate(['/login']);
    return ok;
  };
}
