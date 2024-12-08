export interface ChartDatum {
  name: string;
  value: number;
}

export interface ChartGroup {
  name: string;
  series: ChartDatum[]
}

