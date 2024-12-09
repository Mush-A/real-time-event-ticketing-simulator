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
        return 'bg-gradient-to-b from-green-400 to-green-300 w-1/2 self-end';
      case EventType.TICKET_ADDED:
        return 'bg-gradient-to-b from-blue-400 to-blue-300 w-1/2';
      case EventType.POOL_EMPTY:
        return 'bg-gradient-to-b from-red-500 to-red-400';
      case EventType.POOL_FULL:
        return 'bg-gradient-to-b from-yellow-400 to-yellow-300';
      default:
        return 'bg-gradient-to-b from-gray-300 to-gray-200';
    }
  }

  onClearMessages() {
    this.clearMessages.emit();
  }
}
