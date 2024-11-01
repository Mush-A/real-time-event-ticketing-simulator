export enum TicketStatus {
  AVAILABLE = 'AVAILABLE',
  SOLD = 'SOLD',
  // Add other statuses as needed
}

export interface Ticket {
  id: number;
  status: TicketStatus;
  price: number;
}
