import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Simulation} from '../../models/Simulation';
import {CommonModule} from '@angular/common';
import {NavComponent} from '../common/nav/nav.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    NavComponent
  ],
  templateUrl: './home.component.html',
})
export class HomeComponent implements OnInit {

  simulations: Simulation[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadPreviousSimulations();
  }

  loadPreviousSimulations(): void {
    this.getPreviousSimulations().subscribe({
      next: (data) => {
        this.simulations = data;
      },
      error: (error) => {
        console.error("Error fetching simulations", error);
      }
    });
  }

  getPreviousSimulations() {
    return this.http.get<Simulation[]>("api/simulation/get-all-simulation-config");
  }
}
