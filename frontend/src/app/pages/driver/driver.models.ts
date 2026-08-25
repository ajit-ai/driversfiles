export interface License {
  uuid: string;
  state: string;
  number: string;
  type?: string;
  expiration?: string;
  current: boolean;
}

export interface Residence {
  uuid?: string;
  address1?: string;
  address2?: string;
  city?: string;
  state?: string;
  postalCode?: string;
}

export interface Employment {
  uuid?: string;
  name?: string;
  supervisor?: string;
  address?: string;
  city?: string;
  state?: string;
  postalCode?: string;
  phone?: string;
  position?: string;
  fromDate?: string;
  toDate?: string;
  leaving?: string;
}

export interface Accident {
  uuid?: string;
  accidentDate?: string;
  type?: string;
  nature?: string;
  atFault?: boolean;
  fatalities?: boolean;
  injuries?: boolean;
  damages?: number;
}

export interface Traffic {
  uuid?: string;
  trafficDate?: string;
  city?: string;
  state?: string;
  charge?: string;
  penalty?: string;
}

export interface AccessCodeInfo {
  accessCode: string;
  createdDate?: string;
}
