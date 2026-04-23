import { Injectable, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { GameAction, GameState } from '../models/game-state.model';

@Injectable({ providedIn: 'root' })
export class WebSocketService implements OnDestroy {

  private ws: WebSocket | null = null;
  readonly messages$ = new Subject<GameState>();
  readonly connected$ = new Subject<boolean>();

  connect(): void {
    if (this.ws?.readyState === WebSocket.OPEN) return;
    // Relative URL: works in dev (Angular proxy → :8080) and in Docker (Nginx → backend)
    const protocol = location.protocol === 'https:' ? 'wss:' : 'ws:';
    this.ws = new WebSocket(`${protocol}//${location.host}/game-ws`);

    this.ws.onopen    = () => this.connected$.next(true);
    this.ws.onclose   = () => this.connected$.next(false);
    this.ws.onerror   = () => this.connected$.next(false);
    this.ws.onmessage = (event) => {
      const state: GameState = JSON.parse(event.data);
      this.messages$.next(state);
    };
  }

  send(action: GameAction): void {
    if (this.ws?.readyState === WebSocket.OPEN) {
      this.ws.send(JSON.stringify(action));
    }
  }

  disconnect(): void {
    this.ws?.close();
    this.ws = null;
  }

  ngOnDestroy(): void {
    this.disconnect();
  }
}
