package projet.Model;

import java.util.Objects;

public class Element implements Cell{
	private String cellName;
	private int posX, posY;

	public Element(String cellName, int x, int y) {
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
	@Override
	public boolean isElement(){
		return true;
	}
	
	@Override
	public void update_position(int x, int y) {
		this.posX = x;
		this.posY = y;
		
	}
	@Override
	public String toString() {
		return "" + this.cellName;
	}
	
	
	public void setBaba(boolean b) {
	}
	public void setPushable(boolean b) {
	}
	public void setStop(boolean b) {
	}
	public void setWin(boolean b) {
	}
	public void setOver(boolean b) {
	}
	
}
