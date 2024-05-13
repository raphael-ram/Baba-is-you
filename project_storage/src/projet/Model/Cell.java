package projet.Model;

public interface Cell {

	int posX();
	int posY();
	
	
	default int getPositionX() {
		return posX();	
	}
	
	default int getPositionY() {
		return this.posY();	
	} 
	
	public String property();
	
	default boolean isBaba(){
		return false;
	}
}

