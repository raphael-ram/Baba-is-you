package projet.Model;

import java.util.Objects;

public class Material implements Cell{
	private String cellName;
	private int posX, posY;

	public Material(String cellName, int x, int y) {
		Objects.requireNonNull(cellName);
		this.cellName = cellName;
		this.posX = x;
		this.posY = y;
	}
	
	@Override
	public int posX() {
		return this.posX;
	}
	
	@Override
	public int posY() {
		return this.posY;
	}
	
	public String property() {
		return this.cellName;
	}
	
	@Override
	public String toString() {
		return "" + this.cellName.charAt(0);
	}
}
