import java.util.ArrayList;

/**
 * This class contructs the grid depending of the game level
 */
public class LevelConstruction {

	private ArrayList<String> keywords = new ArrayList<>();

	/**
	 * Depending of the level given it constructs the appropriate grid
	 * @return grid corresponding to the level
	 */
	public Grid startLevelGame(int level) {
		return switch (level) {
		case 1 -> levelOne();
		default -> throw new IllegalArgumentException("Unexpected value: " + level);
		};
	}

	/**
	 * Fill the grid with a special rule at a defined location
	 * @param grid 
	 * @param row index of phrase row
	 * @param start index of column where the rule location starts
	 * @param phrase the rule to fill in the grid
	 */
	public void fillWord(Grid grid, int row, int start, String phrase) {
		String[] splitP = phrase.split(" ");
		for (String s : splitP) {
			grid.setElement(row, start, s);
			if(!keywords.contains(s))
				keywords.add(s);
			start++;
		}
	}

	/**
	 * Level one grid 
	 * @return grid corresponding to level one
	 */
	public Grid levelOne() {
		Grid grid = null;
		grid = new Grid(20);

		// fill grid with the different rules
		fillWord(grid, 1, 2, "BABA IS YOU");
		fillWord(grid, 1, 15, "ROCK IS PUSH");
		fillWord(grid, 18, 2, "WALL IS STOP");
		fillWord(grid, 18, 15, "FLAG IS WIN");

		// fill grid with wall
		for (int i = 0; i <= 11; i++) {
			grid.setElement(7, i + 4, "-");
			grid.setElement(11, i + 4, "-");
		}

		// fill grid with rock
		for (int i = 0; i < 3; i++) {
			grid.setElement(8 + i, 11, "O");
		}
		//set flag element
		grid.setElement(10, 14, "X");
		//set baba element
		grid.setElement(10, 4, "M");
		return grid;
	}
	
	
	/**
	 * Keywords associated to the level
	 * @return keywords of the level
	 */
	public ArrayList<String> getKeyWords(){
		return keywords;
	}

}
