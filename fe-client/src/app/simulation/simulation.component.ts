import {Component} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-simulation',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
  ],
  templateUrl: './simulation.component.html',
  styleUrl: './simulation.component.css'
})
export class SimulationComponent {
  simulationForm: FormGroup;
  message: string | null = null;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    // Initialize the form group with validators
    this.simulationForm = this.fb.group({
      totalTickets: [0, Validators.required],
      ticketReleaseRate: [0, Validators.required],
      customerRetrievalRate: [0, Validators.required],
      maxTicketsCapacity: [0, Validators.required],
      numVendors: [0, Validators.required],
      numCustomers: [0, Validators.required],
      durationInSeconds: [0, Validators.required],
    });
  }

  onSubmit() {
    if (this.simulationForm.valid) {
      // Send a POST request to start the simulation
      this.http.post('api/simulation/start', this.simulationForm.value, { responseType: 'text' })
        .subscribe({
          next: (response: string) => this.message = response,
          error: (error) => this.message = "Error starting simulation: " + error.message
        });
    }
  }

  stopSimulation() {
    // Send a POST request to stop the simulation
    this.http.post('api/simulation/stop', {}, { responseType: 'text' })
      .subscribe({
        next: (response: string) => this.message = response,
        error: (error) => this.message = "Error stopping simulation: " + error.message
      });
  }
}
