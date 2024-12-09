import {Component, EventEmitter, Input, Output} from '@angular/core';
import {EventType, TicketEvent} from '../../../models/TicketEvent';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-event-log',
  standalone: true,
  imports: [
    CommonModule,
  ],
  templateUrl: './event-log.component.html',
})
export class EventLogComponent {
  @Input() messages: TicketEvent[] = [];
  @Output() clearMessages = new EventEmitter<void>();

  getMessageClass(eventType: EventType): string {
    switch (eventType) {
      case EventType.TICKET_PURCHASED:
        return 'bg-green-200 w-1/2 self-end';
      case EventType.TICKET_ADDED:
        return 'bg-blue-200 w-1/2';
      case EventType.POOL_EMPTY:
        return 'bg-red-200';
      case EventType.POOL_FULL:
        return 'bg-yellow-200';
      default:
        return 'bg-gray-200';
    }
  }

  onClearMessages() {
    this.clearMessages.emit();
  }
}
