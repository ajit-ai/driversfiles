import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { API_BASE } from '../../core/auth.service';

export interface AdminUser {
  id: number;
  uuid: string;
  firstName: string;
  lastName: string;
  email: string;
  type: string;
  companyName?: string;
  companyNumber?: string;
}

export interface UserSave {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  password?: string;
  type: string;
  companyName?: string;
  companyNumber?: string;
}

export interface ImportRow {
  id: number;
  importType?: string;
  companyName?: string;
  overwrite?: boolean;
  success?: boolean;
  startTime?: string;
  endTime?: string;
  createdDate?: string;
}

@Injectable({ providedIn: 'root' })
export class AdminDataService {
  private http = inject(HttpClient);
  private base = `${API_BASE}/admin`;

  users(filters: Record<string, string | number>) {
    let params = new HttpParams();
    for (const [k, v] of Object.entries(filters)) {
      if (v !== '' && v != null) params = params.set(k, String(v));
    }
    return this.http.get<{ users: AdminUser[] }>(`${this.base}/users`, { params });
  }

  user = (id: number) => this.http.get<AdminUser>(`${this.base}/users/${id}`);
  saveUser = (u: UserSave) =>
    u.id ? this.http.put<AdminUser>(`${this.base}/users/${u.id}`, u)
         : this.http.post<AdminUser>(`${this.base}/users`, u);
  deleteUser = (id: number) => this.http.delete<void>(`${this.base}/users/${id}`);

  importsMeta = () => this.http.get<{ types: string[]; companies: { id: number; name: string }[] }>(
    `${this.base}/imports/meta`);
  imports = () => this.http.get<ImportRow[]>(`${this.base}/imports`);
  createImport = (form: FormData) => this.http.post<ImportRow>(`${this.base}/imports`, form);
  deleteImport = (id: number) => this.http.delete<void>(`${this.base}/imports/${id}`);

  contentNames = () => this.http.get<{ name: string }[]>(`${this.base}/content`);
  contentNode = (name: string) => this.http.get<{ name: string; content: string }>(
    `${this.base}/content/${name}`);
  saveContent = (name: string, content: string) =>
    this.http.put<void>(`${this.base}/content/${name}`, { content });
}
