package projet.Model;

import java.util.Objects;

public class Material implements Cell {
	private String cellName;
	private int posX, posY;
	private boolean pawnSetting;
	private boolean pushSetting;
	private boolean stopSetting;
	private boolean winSetting;
	private boolean overSetting;
	private boolean reverseSetting;
	private boolean elementSetting;

	public Material(String cellName, int x, int y) {
		this.pawnSetting = false;
		this.reverseSetting = false;
		this.pushSetting = false;
		this.stopSetting = false;
		this.winSetting = false;
		this.overSetting = false;
		this.elementSetting = true;
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
	public void update_position(int x, int y) {
		this.posX = x;
		this.posY = y;

	}

	@Override
	public boolean isPawn() {
		if(this.pawnSetting)
			this.elementSetting = false;
		return this.pawnSetting;
	}
	
	@Override
	public boolean isOver() {
		if(this.overSetting)
			this.elementSetting = false;
		return this.overSetting;
	}

	@Override
	public boolean isPushable() {
		if(this.pushSetting)
			this.elementSetting = false;
		return this.pushSetting;
	}

	@Override
	public boolean isStop() {
		if(this.stopSetting)
			this.elementSetting = false;
		return this.stopSetting;
	}

	@Override
	public boolean isWin() {
		if(this.winSetting)
			this.elementSetting = false;
		return this.winSetting;
	}
	
	@Override
	public boolean isReverse() {
		if(this.reverseSetting)
			this.elementSetting = false;
		return this.reverseSetting;
	}

	@Override
	public boolean isMaterial() {
		return true;
	}

	@Override
	public String toString() {
		return "" + this.cellName.charAt(0);
	}

	@Override
	public boolean isElement() {
		return this.elementSetting;
	}

	public void setPawn(boolean b) {
		if(b)
			this.elementSetting = false;
		this.pawnSetting = b;
	}

	public void setPushable(boolean b) {
		if(b)
			this.elementSetting = false;
		this.pushSetting = b;
	}

	public void setStop(boolean b) {
		if(b)
			this.elementSetting = false;
		this.stopSetting = b;
	}

	public void setWin(boolean b) {
		if(b)
			this.elementSetting = false;
		this.winSetting = b;
	}
	
	public void setReverse(boolean b) {
		if(b)
			this.elementSetting = false;
		this.reverseSetting = b;
	}

	public void setOver(boolean b) {
		if(b)
			this.elementSetting = false;
		this.overSetting = b;
	}
}
