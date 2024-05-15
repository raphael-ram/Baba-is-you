package projet.Model;

import java.util.Objects;

public class Word implements Cell{
	private String cellName;
	private int posX, posY;

	public Word(String cellName, int x, int y) {
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
		return close instanceof Operator;
	}
	
	@Override
	public void update_position(int x, int y) {
		this.posX = x;
		this.posY = y;
		
	}
	
	@Override
	public boolean isBaba() {
		if(cellName.equals("baba"))
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		return "" + this.cellName.charAt(0);
	}
	
	
}
