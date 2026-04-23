import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameService } from '../../core/services/game.service';
import { LEVEL_NAMES } from '../../core/models/game-state.model';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss',
})
export class MenuComponent {
  readonly levels = LEVEL_NAMES;

  constructor(readonly game: GameService) {}

  select(index: number): void {
    this.game.loadLevel(index);
  }
}
