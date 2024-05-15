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
	
	default boolean isBaba(){
		return false;
	}
	default boolean isElement(){
		return false;
	}
}

