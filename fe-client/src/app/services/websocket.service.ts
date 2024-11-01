// src/app/websocket.service.ts
import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient: Client;
  private simulationUpdates: Subject<any> = new Subject<any>();

  constructor(stompClient: Client) {
    this.stompClient = stompClient;
    this.initializeWebSocketConnection();
  }

  initializeWebSocketConnection(): void {
    // Set up the client and specify the broker URL
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws/simulation-websocket'), // WebSocket endpoint
      debug: (str) => console.log(str),
      onConnect: () => {
        this.stompClient.subscribe('/topic/simulation', (message) => {
          if (message.body) {
            this.simulationUpdates.next(JSON.parse(message.body));
          }
        });
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      }
    });

    // Activate the client
    this.stompClient.activate();
  }

  getSimulationUpdates(): Observable<any>{
    return this.simulationUpdates.asObservable()
  }

  disconnect(): void {
    if (this.stompClient && this.stompClient.active) {
      this.stompClient.deactivate();
      console.log('Disconnected from WebSocket');
    }
  }
}
