import { Routes } from '@angular/router';
import {HomeComponent} from './home/home.component';
import {SimulationComponent} from './simulation/simulation.component';

export const routes: Routes = [
  { path: "", component: HomeComponent },
  { path: "simulation", component: SimulationComponent },
];
