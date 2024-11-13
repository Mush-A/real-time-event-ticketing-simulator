import {Ticket} from './Ticket';

export interface User {
  id: number;
  name: string;
  rate: number;
  tickets: Ticket[];
  running: boolean;
  totalTickets: number;
}
