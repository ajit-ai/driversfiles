import { Routes } from '@angular/router';
import { authGuard, roleGuard } from './core/auth.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then((m) => m.LoginComponent)
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./layout/layout.component').then((m) => m.LayoutComponent),
    children: [
      {
        path: 'driver',
        canActivate: [roleGuard('ROLE_DRIVER')],
        loadComponent: () => import('./pages/driver/driver-shell.component').then((m) => m.DriverShellComponent),
        children: [
          { path: '', loadComponent: () => import('./pages/driver/driver-dashboard.component').then((m) => m.DriverDashboardComponent) },
          { path: 'personal-info', loadComponent: () => import('./pages/driver/personal-info/personal-info.component').then((m) => m.PersonalInfoComponent) },
          { path: 'licenses', loadComponent: () => import('./pages/driver/licenses/licenses.component').then((m) => m.LicensesComponent) }
        ]
      },
      {
        path: 'company',
        canActivate: [roleGuard('ROLE_COMPANY')],
        loadComponent: () => import('./pages/coming-soon.component').then((m) => m.ComingSoonComponent)
      },
      {
        path: 'admin',
        canActivate: [roleGuard('ROLE_ADMIN')],
        loadComponent: () => import('./pages/coming-soon.component').then((m) => m.ComingSoonComponent)
      }
    ]
  },
  { path: '**', redirectTo: '' }
];
