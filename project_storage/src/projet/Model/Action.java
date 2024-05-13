package projet.Model;

import java.util.Objects;

public class Action implements Cell {
	private String cellName;
	private int posX, posY;

	public Action(String cellName, int x, int y) {
		Objects.requireNonNull(cellName);
		this.cellName = cellName;
		this.posX = x;
		this.posY = y;
	}
	
	@Override
	public int posX() {
		return posX;
	}
	
	@Override
	public int posY() {
		return posY;
	}
	
	public String property() {
		return this.cellName;
	}
	
	public boolean isNearTo(Cell close) {
		return close instanceof Operator ;
	}
	
	@Override
	public String toString() {
		return "" + this.cellName.charAt(0);
	}
}
