import { Routes } from '@angular/router';
import {HomeComponent} from './home/home.component';
// import {SimulationComponent} from './ui/simulation/simulation.component';
import {SimulationComponent} from './ui/simulator/simulation/simulation.component';

export const routes: Routes = [
  { path: "", component: HomeComponent },
  // { path: "simulation", component: SimulationComponent },
  { path: "new-simulation", component: SimulationComponent },
];
