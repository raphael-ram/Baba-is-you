import { Injectable, signal } from '@angular/core';
import { WebSocketService } from './websocket.service';
import { GameState } from '../models/game-state.model';

@Injectable({ providedIn: 'root' })
export class GameService {

  readonly state   = signal<GameState | null>(null);
  readonly screen  = signal<'menu' | 'game'>('menu');
  readonly connected = signal(false);

  constructor(private ws: WebSocketService) {
    ws.connected$.subscribe(c => this.connected.set(c));
    ws.messages$.subscribe(state => {
      this.state.set(state);
      if (state.status === 'loose') {
        // Give time to see the losing state before returning to menu
        setTimeout(() => this.screen.set('menu'), 1500);
      }
    });
    ws.connect();
  }

  loadLevel(level: number): void {
    this.ws.send({ type: 'LOAD_LEVEL', level });
    this.screen.set('game');
  }

  move(direction: 'UP' | 'DOWN' | 'LEFT' | 'RIGHT'): void {
    this.ws.send({ type: 'MOVE', direction });
  }

  restart(): void {
    this.ws.send({ type: 'RESTART' });
  }

  goToMenu(): void {
    this.screen.set('menu');
  }
}
