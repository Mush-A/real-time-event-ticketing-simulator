import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {Simulation, SimulationForm} from '../../../models/Simulation';

@Component({
  selector: 'app-simulation-config',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './simulation-config.component.html',
})
export class SimulationConfigComponent {
  @Input() isSimulationRunning: boolean = false;
  @Output() formSubmit = new EventEmitter<SimulationForm>();
  @Output() stopSimulation = new EventEmitter<void>();
  @Output() updateSimulation = new EventEmitter<SimulationForm>();

  simulationForm: FormGroup;

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
