import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { API_BASE } from '../../core/auth.service';
import { License } from './driver.models';

@Injectable({ providedIn: 'root' })
export class DriverDataService {
  private http = inject(HttpClient);

  personalInfo = () => this.http.get<PersonalInfo>(`${API_BASE}/driver/me/personal-info`);
  updatePersonalInfo = (body: Partial<PersonalInfo>) =>
    this.http.put<PersonalInfo>(`${API_BASE}/driver/me/personal-info`, body);
  licenses = () => this.http.get<License[]>(`${API_BASE}/driver/me/licenses`);
  addLicense = (l: Omit<License, 'uuid'>) =>
    this.http.post<License>(`${API_BASE}/driver/me/licenses`, l);
  deleteLicense = (uuid: string) =>
    this.http.delete<void>(`${API_BASE}/driver/me/licenses/${uuid}`);
}

export interface PersonalInfo {
  uuid: string;
  firstName: string;
  middleName?: string;
  lastName: string;
  email: string;
}
