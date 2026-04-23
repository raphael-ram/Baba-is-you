export type GameStatus = 'continue' | 'win' | 'loose';

export interface GameState {
  status: GameStatus;
  level: number;
  rows: number;
  cols: number;
  // grid[row][col] = ordered list of cell-type strings (bottom → top)
  grid: string[][][];
}

export interface GameAction {
  type: 'LOAD_LEVEL' | 'MOVE' | 'RESTART';
  direction?: 'UP' | 'DOWN' | 'LEFT' | 'RIGHT';
  level?: number;
}

// Maps a cell-type string to its GIF sprite filename
export const SPRITE_MAP: Record<string, string> = {
  ENTITY_BABA:   'babaEntity.gif',
  ENTITY_FLAG:   'flagEntity.gif',
  ENTITY_WALL:   'wallEntity.gif',
  ENTITY_WATER:  'waterEntity.gif',
  ENTITY_SKULL:  'skullEntity.gif',
  ENTITY_LAVA:   'lavaEntity.gif',
  ENTITY_ROCK:   'rockEntity.gif',
  ENTITY_TILE:   'tileEntity.gif',
  ENTITY_SMILEY: 'smileyEntity.gif',
  WORD_BABA:     'babaWord.gif',
  WORD_FLAG:     'flagWord.gif',
  WORD_WALL:     'wallWord.gif',
  WORD_WATER:    'waterWord.gif',
  WORD_SKULL:    'skullWord.gif',
  WORD_LAVA:     'lavaWord.gif',
  WORD_ROCK:     'rockWord.gif',
  WORD_SMILEY:   'smileyWord.gif',
  WORD_IS:       'isWord.gif',
  WORD_YOU:      'youWord.gif',
  WORD_WIN:      'winWord.gif',
  WORD_STOP:     'stopWord.gif',
  WORD_PUSH:     'pushWord.gif',
  WORD_MELT:     'meltWord.gif',
  WORD_HOT:      'hotWord.gif',
  WORD_DEFEAT:   'defeatWord.gif',
  WORD_SINK:     'sinkWord.gif',
  WORD_JUMP:     'jumpWord.gif',
  EMPTY:         'emptyEntity.gif',
};

export const LEVEL_NAMES = [
  'Level 1', 'Level 2', 'Level 3', 'Level 4',
  'Level 5', 'Level 6', 'Level 7', 'Level 8',
];
