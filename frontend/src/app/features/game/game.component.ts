import {
  Component, ElementRef, HostListener,
  OnInit, OnDestroy, ViewChild
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameService } from '../../core/services/game.service';
import { SPRITE_MAP } from '../../core/models/game-state.model';

const TILE = 48;

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './game.component.html',
  styleUrl: './game.component.scss',
})
export class GameComponent implements OnInit, OnDestroy {

  @ViewChild('canvas',      { static: true }) canvasRef!:  ElementRef<HTMLCanvasElement>;
  @ViewChild('spritesHost', { static: true }) spritesHost!: ElementRef<HTMLDivElement>;

  private ctx!: CanvasRenderingContext2D;
  private images: Record<string, HTMLImageElement> = {};
  private rafId = 0;

  constructor(readonly game: GameService) {}

  ngOnInit(): void {
    this.ctx = this.canvasRef.nativeElement.getContext('2d')!;
    this.preloadSprites();
    this.startRenderLoop();
  }

  ngOnDestroy(): void {
    cancelAnimationFrame(this.rafId);
  }

  @HostListener('window:keydown', ['$event'])
  onKey(event: KeyboardEvent): void {
    if (this.game.screen() !== 'game') return;
    switch (event.key) {
      case 'ArrowUp':    this.game.move('UP');    break;
      case 'ArrowDown':  this.game.move('DOWN');  break;
      case 'ArrowLeft':  this.game.move('LEFT');  break;
      case 'ArrowRight': this.game.move('RIGHT'); break;
      case 'r': case 'R': this.game.restart();   break;
      case 'Escape':     this.game.goToMenu();    break;
    }
    if (['ArrowUp','ArrowDown','ArrowLeft','ArrowRight'].includes(event.key)) {
      event.preventDefault();
    }
  }

  private preloadSprites(): void {
    const host = this.spritesHost.nativeElement;
    // Chaque <img> est inséré dans le DOM (visibility:hidden) pour que le browser
    // avance les frames GIF. drawImage() capture ensuite la frame courante à chaque tick.
    Object.values(SPRITE_MAP).forEach(file => {
      const img = new Image();
      img.src = `/images/${file}`;
      host.appendChild(img);
      this.images[file] = img;
    });
  }

  private startRenderLoop(): void {
    const loop = () => {
      const state = this.game.state();
      if (state) this.render(state);
      this.rafId = requestAnimationFrame(loop);
    };
    this.rafId = requestAnimationFrame(loop);
  }

  private render(state: ReturnType<typeof this.game.state>): void {
    if (!state) return;
    const canvas = this.canvasRef.nativeElement;

    const w = state.cols * TILE;
    const h = state.rows * TILE;
    if (canvas.width !== w)  canvas.width  = w;
    if (canvas.height !== h) canvas.height = h;

    this.ctx.fillStyle = '#000';
    this.ctx.fillRect(0, 0, w, h);

    for (let r = 0; r < state.rows; r++) {
      for (let c = 0; c < state.cols; c++) {
        const stack = state.grid[r][c];
        const x = c * TILE;
        const y = r * TILE;
        for (const cellType of stack) {
          const file = SPRITE_MAP[cellType] ?? SPRITE_MAP['EMPTY'];
          const img  = this.images[file];
          if (img?.complete && img.naturalWidth > 0) {
            this.ctx.drawImage(img, x, y, TILE, TILE);
          }
        }
      }
    }

    if (state.status === 'win') {
      this.drawOverlay('YOU WIN! 🎉', '#4caf5088');
    } else if (state.status === 'loose') {
      this.drawOverlay('YOU LOSE', '#f4433688');
    }
  }

  private drawOverlay(text: string, bg: string): void {
    const { width, height } = this.canvasRef.nativeElement;
    this.ctx.fillStyle = bg;
    this.ctx.fillRect(0, 0, width, height);
    this.ctx.fillStyle = '#fff';
    this.ctx.font = `bold ${TILE}px 'Courier New'`;
    this.ctx.textAlign = 'center';
    this.ctx.textBaseline = 'middle';
    this.ctx.fillText(text, width / 2, height / 2);
  }
}
