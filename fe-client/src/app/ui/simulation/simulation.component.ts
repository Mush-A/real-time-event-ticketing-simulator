import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {CommonModule} from '@angular/common';
import {WebSocketService} from '../../services/websocket.service';
import {BehaviorSubject} from 'rxjs';
import {EventType, TicketEvent} from '../../models/TicketEvent';
import {SimulationStatusType} from '../../models/Simulation';
import {Color, NgxChartsModule, ScaleType} from '@swimlane/ngx-charts';
import {UserType} from '../../models/User';
import {ChartDatum, ChartGroup} from '../../models/ChartDatum';

@Component({
  selector: 'app-simulation',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    NgxChartsModule,
  ],
  templateUrl: './simulation.component.html',
  styleUrls: ['./simulation.component.css']
})
export class SimulationComponent implements OnInit {
  simulationForm: FormGroup;
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
  chartColorScheme : Color = {
    name: 'myScheme',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#f00', '#0f0', '#0ff', '#ff0', '#f0f', '#00f', '#50e', '#050', '#055', '#550', '#505']
  };

  constructor(private fb: FormBuilder, private http: HttpClient, private ws: WebSocketService) {
    // Initialize the form group with validators
    this.simulationForm = this.fb.group({
      totalTickets: [100, [Validators.required, Validators.min(1)]],
      ticketReleaseRate: [500, [Validators.required, Validators.min(1)]],
      customerRetrievalRate: [500, [Validators.required, Validators.min(1)]],
      maxTicketsCapacity: [100, [Validators.required, Validators.min(1)]],
      numVendors: [1, [Validators.required, Validators.min(1)]],
      numCustomers: [1, [Validators.required, Validators.min(1)]],
    });

    // Subscribe to the WebSocket service to receive updates
    this.ws.getSimulationUpdates().subscribe({
      next: (update) => {
        // console.log('WebSocket update received: ' + JSON.stringify(update));
        // Push new messages to the BehaviorSubject
        this.messagesSubject.next([...this.messagesSubject.value, update]);

        this.groupEventsByUserType(this.messagesSubject.value);

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

  onSubmit() {
    if (this.simulationForm.valid) {
      // Send a POST request to start the simulation
      this.http.post('api/simulation/start', this.simulationForm.value, { responseType: 'text' })
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

  updateSimulation() {
    // Send a POST request to update the simulation
    this.http.put('api/simulation/update', this.simulationForm.value, { responseType: 'text' })
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

  getMessageClass(eventType: EventType): string {
    switch (eventType) {
      case EventType.TICKET_PURCHASED:
        return 'bg-green-200 w-1/2 self-end';
      case EventType.TICKET_ADDED:
        return 'bg-blue-200 w-1/2';
      case EventType.POOL_EMPTY:
        return 'bg-red-200';
      case EventType.POOL_FULL:
        return 'bg-yellow-200';
      default:
        return 'bg-gray-200';
    }
  }
}
