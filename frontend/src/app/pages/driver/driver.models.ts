export interface License {
  uuid: string;
  state: string;
  number: string;
  type?: string;
  expiration?: string;
  current: boolean;
}
