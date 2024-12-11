import { Ticket } from './Ticket';
import {User} from './User';
import {Simulation} from './Simulation';

export enum EventType {
  TICKET_ADDED = 'TICKET_ADDED',
  TICKET_PURCHASED = 'TICKET_PURCHASED',
  POOL_FULL = 'POOL_FULL',
  POOL_EMPTY = 'POOL_EMPTY',
  SIMULATION_OVER = 'SIMULATION_OVER'
}

export interface TicketEvent {
  eventType: EventType;
  message: string;
  user?: User;
  ticket?: Ticket;
  simulationConfig: Simulation;
}
