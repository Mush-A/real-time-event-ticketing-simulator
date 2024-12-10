export enum SimulationStatusType {
  RUNNING = 'RUNNING',
  NOT_RUNNING = 'NOT_RUNNING',
  NO_SIMULATION = 'NO_SIMULATION',
  SIMULATION_OVER = 'SIMULATION_OVER'
}


export interface Simulation {
  totalTickets: number;
  ticketReleaseRate: number;
  customerRetrievalRate: number;
  maxTicketsCapacity: number;
  numVendors: number;
  numCustomers: number;
}
