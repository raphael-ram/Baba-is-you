import java.util.ArrayList;

/***
 * This class is about the grid of the game, we should be able to set a grid according to the level
 * The toString is particular ()
 */
/**
 * 
 */
public class Grid {

	private ArrayList<ArrayList<String>> grid = new ArrayList<ArrayList<String>>();

	private static int size;

	public Grid(int size) {
		if (size < 10) {
			throw new IllegalArgumentException("size given is too short");
		}

		Grid.size = size;

		for (int i = 0; i < size; i++) {
			ArrayList<String> row = new ArrayList<>();
			for (int j = 0; j < size; j++) {
				row.add("vide");
			}
			grid.add(row);
		}

	}

	/**
	 * Getter of the game grid
	 * 
	 * @return grid
	 */
	public ArrayList<ArrayList<String>> getGrid() {
		return grid;
	}

	/**
	 * Return size of grid game
	 * 
	 * @return size
	 */
	public static int getSize() {
		return size;
	}

	/**
	 * Set an element at a position
	 * 
	 * @param indexI  index of row
	 * @param indexJ  index of column
	 * @param element element which should be placed
	 */
	public void setElement(int indexI, int indexJ, String element) {
		grid.get(indexI).set(indexJ, element);
	}

///VOIR SI CES GETTERS SONT NECESSAIRES

	/**
	 * Return grid to string with the associated keywords
	 * 
	 * @param keywords
	 * @return grid as a string
	 */
	public String toString(ArrayList<String> keywords) {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < Grid.size; i++) {
			for (int j = 0; j < Grid.size; j++) {
				if (grid.get(i).get(j).equals("vide")) {
					result.append("*\t");
				} else if (keywords.contains(grid.get(i).get(j))) {
					result.append(grid.get(i).get(j).charAt(0)).append("\t");
				} else {
					result.append(grid.get(i).get(j)).append("\t");
				}
			}
			result.append("\n");
		}
		return result.toString();

	}

}
