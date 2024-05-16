package projet.Model;

import java.util.Objects;

public class Material implements Cell{
	private String cellName;
	private int posX, posY;
	private boolean babaSetting;
	private boolean pushSetting;
	private boolean stopSetting;
	private boolean winSetting;

	public Material(String cellName, int x, int y) {
		this.babaSetting = false;
		this.pushSetting = false;
		this.stopSetting = false;
		this.winSetting = false;
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
	public boolean isBaba() {
		return this.babaSetting;
	}
	
	
	@Override
	public boolean isPushable() {
		return this.pushSetting;
	}
	
	@Override
	public boolean isStop() {
		return this.stopSetting;
	}
	
	@Override
	public boolean isWin() {
		return this.winSetting;
	}
	
	@Override
	public boolean isMaterial() {
		return true;
	}
	
	
	@Override
	public String toString() {
		return "" + this.cellName.charAt(0);
	}
	
	public void setBaba(boolean b) {
		this.babaSetting = b;
	}
	
	public void setPushable(boolean b) {
		this.pushSetting = b;
	}
	
	public void setStop(boolean b) {
		this.stopSetting = b;
	}
	
	public void setWin(boolean b) {
		this.winSetting = b;
	}
}
