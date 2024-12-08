import {Ticket} from './Ticket';

export enum UserType {
  VENDOR = 'VENDOR',
  CUSTOMER = 'CUSTOMER'
}

export interface User {
  id: number;
  name: string;
  rate: number;
  tickets: Ticket[];
  running: boolean;
  totalTickets: number;
  type: UserType;
}
