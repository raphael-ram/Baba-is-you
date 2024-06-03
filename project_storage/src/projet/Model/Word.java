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
	public boolean isPushable() {
		return true;
	}
	
	public String identity() {
		return "word";
	}

	@Override
	public boolean isProperty() {
		return true;
	}

	@Override
	public String nextIdentity() {
		return "operator";
	}

	
	@Override
	public String toString() {
		return "" + this.cellName.charAt(0);
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
	public void setReverse(boolean b) {
	}
}
