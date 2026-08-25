import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { API_BASE } from '../../core/auth.service';

export interface CompanyProfile {
  uuid: string; name: string; companyNumber?: string;
  address1?: string; address2?: string; city?: string; state?: string;
  postalCode?: string; phone?: string; fax?: string; website?: string;
}
export interface GrantedDriver { email: string; name: string; company?: string; grantedDate?: string; }

@Injectable({ providedIn: 'root' })
export class CompanyDataService {
  private http = inject(HttpClient);
  private base = `${API_BASE}/company/me`;

  profile = () => this.http.get<CompanyProfile>(`${this.base}/profile`);
  updateProfile = (p: Partial<CompanyProfile>) => this.http.put<CompanyProfile>(`${this.base}/profile`, p);
  drivers = () => this.http.get<GrantedDriver[]>(`${this.base}/drivers`);
}