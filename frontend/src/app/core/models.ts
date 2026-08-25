export interface UserInfo {
  uuid: string;
  email: string;
  firstName: string;
  lastName: string;
  type: 'ADMIN' | 'COMPANY' | 'DRIVER';
  roles: string[];
}

export interface LoginResponse {
  token: string;
  user: UserInfo;
}

export function decodeJwtPayload(token: string): any | null {
  try {
    const payload = token.split('.')[1];
    return JSON.parse(atob(payload.replace(/-/g, '+').replace(/_/g, '/')));
  } catch {
    return null;
  }
}

export function isTokenExpired(token: string): boolean {
  const payload = decodeJwtPayload(token);
  if (!payload?.exp) return true;
  return payload.exp * 1000 < Date.now();
}
