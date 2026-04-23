import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameService } from './core/services/game.service';
import { MenuComponent } from './features/menu/menu.component';
import { GameComponent } from './features/game/game.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, MenuComponent, GameComponent],
  template: `
    @if (game.screen() === 'menu') {
      <app-menu />
    } @else {
      <app-game />
    }
  `,
})
export class App {
  constructor(readonly game: GameService) {}
}
