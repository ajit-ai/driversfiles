import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { API_BASE } from '../../core/auth.service';
import { AccessCodeInfo, Accident, Employment, License, Residence, Traffic } from './driver.models';

@Injectable({ providedIn: 'root' })
export class DriverDataService {
  private http = inject(HttpClient);
  private base = `${API_BASE}/driver/me`;

  personalInfo = () => this.http.get<PersonalInfo>(`${this.base}/personal-info`);
  updatePersonalInfo = (body: Partial<PersonalInfo>) =>
    this.http.put<PersonalInfo>(`${this.base}/personal-info`, body);
  licenses = () => this.http.get<License[]>(`${this.base}/licenses`);
  addLicense = (l: Omit<License, 'uuid'>) => this.http.post<License>(`${this.base}/licenses`, l);
  deleteLicense = (uuid: string) => this.http.delete<void>(`${this.base}/licenses/${uuid}`);

  residences = () => this.http.get<Residence[]>(`${this.base}/residences`);
  addResidence = (r: Residence) => this.http.post<Residence>(`${this.base}/residences`, r);
  updateResidence = (uuid: string, r: Residence) =>
    this.http.put<Residence>(`${this.base}/residences/${uuid}`, r);
  deleteResidence = (uuid: string) => this.http.delete<void>(`${this.base}/residences/${uuid}`);

  employments = () => this.http.get<Employment[]>(`${this.base}/employments`);
  addEmployment = (e: Employment) => this.http.post<Employment>(`${this.base}/employments`, e);
  deleteEmployment = (uuid: string) => this.http.delete<void>(`${this.base}/employments/${uuid}`);

  accidents = () => this.http.get<Accident[]>(`${this.base}/accidents`);
  addAccident = (a: Accident) => this.http.post<Accident>(`${this.base}/accidents`, a);
  deleteAccident = (uuid: string) => this.http.delete<void>(`${this.base}/accidents/${uuid}`);

  traffics = () => this.http.get<Traffic[]>(`${this.base}/traffics`);
  addTraffic = (t: Traffic) => this.http.post<Traffic>(`${this.base}/traffics`, t);
  deleteTraffic = (uuid: string) => this.http.delete<void>(`${this.base}/traffics/${uuid}`);

  accessCode = () => this.http.get<AccessCodeInfo>(`${this.base}/access-code`);
}

export interface PersonalInfo {
  uuid: string;
  firstName: string;
  middleName?: string;
  lastName: string;
  email: string;
}
