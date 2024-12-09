import { Component, EventEmitter, Output } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {CommonModule} from '@angular/common';

export interface SimulationSettingsFormData {
  totalTickets: number;
  ticketReleaseRate: number;
  customerRetrievalRate: number;
  maxTicketsCapacity: number;
  numVendors: number;
  numCustomers: number;
}

@Component({
  selector: 'app-simulation-settings',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './simulation-settings.component.html',
})
export class SimulationSettingsComponent {
  @Output() formSubmit = new EventEmitter<SimulationSettingsFormData>();
  @Output() stopSimulation = new EventEmitter<void>();
  @Output() updateSimulation = new EventEmitter<SimulationSettingsFormData>();

  simulationForm: FormGroup;
  isSimulationRunning = false;

  constructor(private fb: FormBuilder) {
    this.simulationForm = this.fb.group({
      totalTickets: [100, [Validators.required, Validators.min(1)]],
      ticketReleaseRate: [500, [Validators.required, Validators.min(1)]],
      customerRetrievalRate: [500, [Validators.required, Validators.min(1)]],
      maxTicketsCapacity: [100, [Validators.required, Validators.min(1)]],
      numVendors: [1, [Validators.required, Validators.min(1)]],
      numCustomers: [1, [Validators.required, Validators.min(1)]],
    });
  }

  onSubmit() {
    if (this.simulationForm.valid) {
      this.formSubmit.emit(this.simulationForm.value);
    }
  }

  onStop() {
    this.stopSimulation.emit();
  }

  onUpdate() {
    this.updateSimulation.emit(this.simulationForm.value);
  }
}
