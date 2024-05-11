package src.fr.uge.memory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * The SimpleGameData class stores all relevant pieces of information for the
 * game status.
 * 
 * @author vincent
 */
public class SimpleGameData {
	/**
	 * 2D array storing cells that represent cards to remember.
	 */
	private final Word[][] word;
	/**
	 * First cell that was clicked on and shall be matched.
	 */
	/**
	 * Number of pairs that have already been matched.
	 */
	private int wins;

	/**
	 * Creates and initializes a new GameData with a given number of lines and
	 * columns.
	 * 
	 * @param nbLines   Height of the game grid.
	 * @param nbColumns Width of the game grid.
	 */
	public SimpleGameData(int nbLines, int nbColumns) {
		if (nbLines < 0 || nbColumns < 0) {
			throw new IllegalArgumentException();
		}
		word = new Word[nbLines][nbColumns];
		wins = 0;
	}
	
	/**
	 * Gets the number of lines in the matrix contained in this GameData.
	 * 
	 * @return Number of lines in the matrix.
	 */
	public int lines() {
		return word.length;
	}

	/**
	 * Gets the number of columns in the matrix contained in this GameData.
	 * 
	 * @return Number of columns in the matrix.
	 */
	public int columns() {
		return word[0].length;
	}

	/**
	 * Gets the ID of the cell specified by its row and column.
	 * 
	 * @param i Row index of the cell.
	 * @param j Column index of the cell.
	 * @return ID of the cell.
	 */
	public int id(int i, int j) {
		return word[i][j].id();
	}
	public void setImageAt(int x,int y, Word imageId) {
		word[x][y] = imageId;
	}
	public Word getImageAt(int x,int y) {
		return word[x][y];
	}
	public void move(Direction d) {
		switch(d) {
		case UP -> moveUp();
		case DOWN -> moveDown();
		case LEFT -> moveLeft();
		case RIGHT -> moveRight();
		}
	}
	public void moveUp() {}
	public void moveDown() {}
	public void moveLeft() {}
	public void moveRight() {}

	
	/**
	 * Tests if the player has won.
	 * 
	 * @return True if the player has won by finding all pairs of objects, and False
	 *         otherwise.
	 */
	public boolean win() {
		return 2 * wins == lines() * columns();
	}
	
	public void loadFromTextFile(String resourcePath, ImageLoader loader) throws IOException {
	    try ( var inputStream = SimpleGameData.class.getResourceAsStream("/images/"+resourcePath);
	    		var reader = new BufferedReader(new InputStreamReader(inputStream))) {
	        String line;
	        int lineIndex = 0;
	        while ((line = reader.readLine()) != null && lineIndex < word.length) {
	            for (int colIndex = 0; colIndex < line.length() && colIndex < word[lineIndex].length; colIndex++) {
	                char ch = line.charAt(colIndex);
	                if (ch != '-') {
	                    loader.loadImageForCharacter(ch);
	                    int id = loader.getImageId(ch);
	                    if (id != -1) {
	                        word[lineIndex][colIndex] = new Name(Character.toString(ch), id);
	                    }
	                }
	            }
	            lineIndex++;
	        }
	    }
	}
}