import { Component, Input, Output, EventEmitter } from '@angular/core';
import {Color, NgxChartsModule, ScaleType} from '@swimlane/ngx-charts';
import {ChartGroup} from '../../../models/ChartDatum';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    NgxChartsModule,
  ],
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent {
  @Input() chartData: ChartGroup[] = [];
  @Output() clearChartData = new EventEmitter<void>();

  chartColorScheme: Color = {
    name: 'myScheme',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#f00', '#0f0', '#0ff', '#ff0', '#f0f', '#00f', '#50e', '#050', '#055', '#550', '#505']
  };

  onClearChartData() {
    this.clearChartData.emit();
  }
}
