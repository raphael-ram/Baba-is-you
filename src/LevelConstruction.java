import java.util.ArrayList;

public class LevelConstruction {

	private int level;
	private ArrayList<String> keywords = new ArrayList<>();

	public LevelConstruction(int level) {
		if (level <= 0 || level > 7) {
			throw new IllegalArgumentException("level given is not correct");
		}
		this.level = level;
	}

	public Grid startLevelGame() {
		return switch (this.level) {
		case 1 -> levelOne();
		default -> throw new IllegalArgumentException("Unexpected value: " + this.level);
		};
	}

	public void fillWord(Grid grid, int row, int start, String phrase) {
		String[] splitP = phrase.split(" ");
		for (String s : splitP) {
			grid.setElement(row, start, s);
			if(!keywords.contains(s))
				keywords.add(s);
			start++;
		}
	}

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
	
	public ArrayList<String> getKeyWords(){
		return keywords;
	}

}
