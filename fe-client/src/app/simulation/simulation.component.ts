import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { WebSocketService } from '../services/websocket.service';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-simulation',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
  ],
  templateUrl: './simulation.component.html',
  styleUrls: ['./simulation.component.css']
})
export class SimulationComponent {
  simulationForm: FormGroup;
  private messagesSubject = new BehaviorSubject<string[]>([]);
  messages$ = this.messagesSubject.asObservable();

  constructor(private fb: FormBuilder, private http: HttpClient, private ws: WebSocketService) {
    // Initialize the form group with validators
    this.simulationForm = this.fb.group({
      totalTickets: [1000, Validators.required],
      ticketReleaseRate: [1000, Validators.required],
      customerRetrievalRate: [1000, Validators.required],
      maxTicketsCapacity: [1000, Validators.required],
      numVendors: [1, Validators.required],
      numCustomers: [1, Validators.required],
      durationInSeconds: [10, Validators.required],
    });

    // Subscribe to the WebSocket service to receive updates
    this.ws.getSimulationUpdates().subscribe({
      next: (update) => {
        // Push new messages to the BehaviorSubject
        this.messagesSubject.next([...this.messagesSubject.value, update]);
      },
      error: (error) => {
        console.error('Error receiving WebSocket update: ' + error);
      }
    });
  }

  onSubmit() {
    if (this.simulationForm.valid) {
      // Send a POST request to start the simulation
      this.http.post('api/simulation/start', this.simulationForm.value, { responseType: 'text' })
        .subscribe({
          next: (response: string) => {
            // Push response as a message
            this.messagesSubject.next([...this.messagesSubject.value, response]);
          },
          error: (error) => {
            this.messagesSubject.next([...this.messagesSubject.value, "Error starting simulation: " + error.message]);
          }
        });
    }
  }

  stopSimulation() {
    // Send a POST request to stop the simulation
    this.http.post('api/simulation/stop', {}, { responseType: 'text' })
      .subscribe({
        next: (response: string) => {
          this.messagesSubject.next([...this.messagesSubject.value, response]);
        },
        error: (error) => {
          this.messagesSubject.next([...this.messagesSubject.value, "Error stopping simulation: " + error.message]);
        }
      });
  }
}
