import { Routes } from '@angular/router';
import {HomeComponent} from './ui/home/home.component';
import {SimulationComponent} from './ui/simulator/simulation/simulation.component';

export const routes: Routes = [
  { path: "", component: HomeComponent },
  { path: "simulation", component: SimulationComponent },
];
