import java.util.Set;
import java.util.HashSet;

/**
 * The Cell class deals with minimal information about cells of the memory game.
 * 
 * @author Vincent Jugé
 *
 */
public class Cell {
	
	/**
	 *  ID of the card represented by the cell.
	 */
	private final int id;
	
	
	
	
	
	
	/**
	 *  Visibility status of the card represented by the cell.
	 */
	private boolean visible;
	
	
	
	
	
	/**
	 * 
	 */
	private boolean isActive;
	
	
	
	
	/**
	 * Property associated to the card represented by the cell.
	 */
	private Property property;

	/**
	 * Creates a new cell containing a card to to remember.
	 * 
	 * @param i ID of the card to remember.
	 */
	public Cell(int i,Property prop) {
		id = i;
		visible = false;
		property = prop;
		isActive = false;
	}
	
	/**
	 * Makes the card's property active
	 */
	public void activate() {
		isActive = true;
	}
	/**
	 * Makes the card's property inactive
	 */
	public void desactivate() {
		isActive = false;
	}
	
	/**
	 * Gets the state of the card's Property
	 * @return true if the property of the card is active. Otherwise false
	 */
	public boolean active() {
		return isActive;
	}
	
	
	
	
	

	/**
	 * Makes the card invisible, which shall result in showing the card's back.
	 */
	public void hide() {
		visible = false;
	}

	/**
	 * Makes the card visible, which shall result in showing the card's face.
	 */
	public void show() {
		visible = true;
	}

	/**
	 * Gets the ID of the object to remember.
	 * 
	 * @return ID of the object to remember.
	 */
	public int id() {
		return id;
	}
	
	
	

	/**
	 * Gets the state (visible or invisible) of the cell.
	 * 
	 * @return True if the card is visible, false otherwise.
	 */
	public boolean visible() {
		return visible;
	}
	
	
	
	
	
	
}
