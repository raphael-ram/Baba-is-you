package fr.umlv.babaisyou.model;

public enum Direction {
	Down(1, 0),
  Up(-1, 0),
  Right(0, 1),
  Left(0, -1);

  final public int x, y;

  Direction(int x, int y) {
      this.x = x;
      this.y = y;
  }
}
