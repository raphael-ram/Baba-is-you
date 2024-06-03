package projet.Model;

public interface Cell {

	int posX();
	int posY();
	
	
	default int getPositionX() {
		return this.posX();	
	}
	
	default int getPositionY() {
		return this.posY();	
	} 
	
	public void update_position(int x, int y);

	public String property();
	
	public void setBaba(boolean b);
	public void setPushable(boolean b);
	public void setStop(boolean b);
	public void setWin(boolean b);
	public void setOver(boolean b);
	public void setReverse(boolean b);
	
	default boolean isPawn(){
		return false;
	}
	
	default boolean isElement(){
		return false;
	}
	default boolean isOver(){
		return false;
	}
	
	default boolean isWin(){
		return false;
	}
	
	default boolean isReverse(){
		return false;
	}
	
	default boolean isProperty(){
		return false;
	}
	
	default boolean isMaterial(){
		return false;
	}
	
	default boolean isStop(){
		return false;
	}
	default String identity() {
		return "";
	}
	
	default String nextIdentity() {
		return "";
	}
	
	default boolean isAcessible() {
		return true;
	}
	
	default boolean isPushable() {
		return false;
	}
}

