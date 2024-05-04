import java.util.ArrayList;
import java.util.Objects;
/***
 * This class is about the grid of the game, we should be able to set a grid according to the level
 * The toString is particular ()
 * Reflexion about maintaining keywords in constructor
 */
public class Grid {

	private ArrayList<ArrayList<String>> grid = new ArrayList<ArrayList<String>>();

	private static int size;
	private static int level;
	private ArrayList<String> keywords = new ArrayList<>();

	public Grid(int size, int level, ArrayList<String> keywords) {
		Objects.requireNonNull(keywords);
		if (size < 10) {
			throw new IllegalArgumentException("size given is too short");
		}

		if (level < 0 || level > 7) {
			throw new IllegalArgumentException("level given is not correct");
		}

		this.keywords = keywords;
		Grid.size = size;
		Grid.level = level;

		for (int i = 0; i < size; i++) {
			ArrayList<String> row = new ArrayList<>();
			for (int j = 0; j < size; j++) {
				row.add("vide");
			}
			grid.add(row);
		}

	}

	public ArrayList<ArrayList<String>> getGrid() {
		return grid;
	}

	public static int getSize() {
		return size;
	}

	public static int getLevel() {
		return level;
	}
	
	public void setElement(int indexI,int indexJ, String element) {
		grid.get(indexI).set(indexJ, element);
	}

///VOIR SI CES GETTERS SONT NECESSAIRES


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
