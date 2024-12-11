import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CommonModule} from '@angular/common';
import {BehaviorSubject} from 'rxjs';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import {EventType, TicketEvent} from '../../../models/TicketEvent';
import {ChartDatum, ChartGroup} from '../../../models/ChartDatum';
import {WebSocketService} from '../../../services/websocket.service';
import {SimulationForm, SimulationStatusType} from '../../../models/Simulation';
import {UserType} from '../../../models/User';
import {
  SimulationConfigComponent
} from '../simulation-config/simulation-config.component';
import {EventLogComponent} from '../event-log/event-log.component';
import {DashboardComponent} from '../dashboard/dashboard.component';
import {NavComponent} from '../../common/nav/nav.component';

@Component({
  selector: 'app-simulation',
  standalone: true,
  imports: [
    CommonModule,
    NgxChartsModule,
    SimulationConfigComponent,
    EventLogComponent,
    DashboardComponent,
    NavComponent,
  ],
  templateUrl: './simulation.component.html',
})
export class SimulationComponent implements OnInit {
  private messagesSubject = new BehaviorSubject<TicketEvent[]>([]);
  messages$ = this.messagesSubject.asObservable();
  isSimulationRunning = false;
  display: "eventLog" | "dashboard" = "eventLog";
  chartData: ChartGroup[] = [
    {
      name: 'Vendors',
      series: []
    },
    {
      name: 'Customers',
      series: []
    }
  ];

  constructor(private http: HttpClient, private ws: WebSocketService) {
    // Subscribe to the WebSocket service to receive updates
    this.ws.getSimulationUpdates().subscribe({
      next: (update) => {
        // console.log('WebSocket update received: ' + JSON.stringify(update));
        // Push new messages to the BehaviorSubject
        this.messagesSubject.next([...this.messagesSubject.value, update]);

        this.groupEventsByUserType(this.messagesSubject.value);

        console.log('Event', update);

        if (update.eventType === EventType.SIMULATION_OVER) {
          // If the simulation is over, check the current simulation status
          this.setIsSimulationRunning();
        }
      },
      error: (error) => {
        console.error('Error receiving WebSocket update: ' + error);
      }
    });
  }

  ngOnInit() {
    this.loadHistoryEvents();
    // Check the current simulation status
    this.setIsSimulationRunning();
  }

  handleFormSubmit(event: SimulationForm) {
    this.http.post('api/simulation/start', event, { responseType: 'text' })
      .subscribe({
        next: (response: string) => {
          console.log('Simulation started: ' + response);
          this.setIsSimulationRunning();
        },
        error: (error) => {
          console.error('Error starting simulation: ' + error);
        }
      });
    this.clearMessages();
  }

  stopSimulation() {
    // Send a POST request to stop the simulation
    this.http.post('api/simulation/stop', {}, { responseType: 'text' })
      .subscribe({
        next: (response: string) => {
          console.log('Simulation stopped: ' + response);
          this.setIsSimulationRunning();
        },
        error: (error) => {
          console.error('Error stopping simulation: ' + error);
        }
      });
  }

  handleUpdateSimulation(event: SimulationForm) {
    // Send a POST request to update the simulation
    this.http.put('api/simulation/update', event, { responseType: 'text' })
      .subscribe({
        next: (response: string) => {
          console.log('Simulation updated: ' + response);
        },
        error: (error) => {
          console.error('Error updating simulation: ' + error);
        }
      });
  }

  clearMessages() {
    // Clear the messages in the BehaviorSubject
    this.messagesSubject.next([]);
  }

  clearChartData() {
    // Clear the chart data
    this.chartData = [
      {
        name: 'Vendors',
        series: []
      },
      {
        name: 'Customers',
        series: []
      }
    ];
  }

  getSimulationStatus() {
    // Send a GET request to get the current simulation status
    return this.http.get<SimulationStatusType>('api/simulation/status');
  }

  getTicketEvents() {
    // Send a GET request to get the ticket events
    return this.http.get<TicketEvent[]>('api/simulation/ticket-events');
  }

  loadHistoryEvents() {
    // Load the ticket events
    this.getTicketEvents().subscribe({
      next: (events: TicketEvent[]) => {
        // console.log('Ticket events: ' + JSON.stringify(events));
        // Push the events to the BehaviorSubject
        this.messagesSubject.next(events);
        this.groupEventsByUserType(events);
      },
      error: (error) => {
        console.error('Error getting ticket events: ' + error);
      }
    });
  }

  setIsSimulationRunning() {
    this.getSimulationStatus().subscribe({
      next: (status: SimulationStatusType) => {
        this.isSimulationRunning = status === SimulationStatusType.RUNNING;
        console.log('Simulation status: ' + status);
      },
      error: (error) => {
        console.error('Error getting simulation status: ' + JSON.stringify(error));
      }
    })
  }

  // Group events by user type (Vendor/Customer) and event type
  groupEventsByUserType(events: TicketEvent[]) {
    const userEvents = events.filter(
      (event) =>
        event.eventType === EventType.TICKET_ADDED || event.eventType === EventType.TICKET_PURCHASED
    );

    const vendorEvents = userEvents.filter((event) => event.user?.type === UserType.VENDOR);
    const customerEvents = userEvents.filter((event) => event.user?.type === UserType.CUSTOMER);

    const newVendorSeries : ChartDatum[] = [];
    const newCustomerSeries : ChartDatum[] = [];

    vendorEvents.forEach((event) => {
      const existingEntry = newVendorSeries.find((e) => e.name === event.user?.name);
      if (!existingEntry) {
        newVendorSeries.push({ name: event.user?.name ?? '', value: 1 });
      } else {
        existingEntry.value = event.user?.numberOfTickets ?? 0;
      }
    });

    customerEvents.forEach((event) => {
      const existingEntry = newCustomerSeries.find((e) => e.name === event.user?.name);
      if (!existingEntry) {
        newCustomerSeries.push({ name: event.user?.name ?? '', value: 1 });
      } else {
        existingEntry.value = event.user?.numberOfTickets ?? 0;
      }
    });

    // Reassign the chart data to trigger change detection
    this.chartData = [
      { name: 'Vendors', series: newVendorSeries },
      { name: 'Customers', series: newCustomerSeries },
    ];
  }
}
