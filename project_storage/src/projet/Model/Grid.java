package projet.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class Grid {

	/***
	 * This class is about the grid of the game, we should be able to set a grid
	 * according to the level The toString is particular ()
	 */
	/**
	 * 
	 */
	private static ArrayList<ArrayList<Cell>> grid = new ArrayList<ArrayList<Cell>>();

	public static void launchingData(Path path) throws IOException {
		Objects.requireNonNull(path);
		try (var reader = Files.newBufferedReader(path)) {

			String line;
			int x = 0;
			int y = 0;
			while ((line = reader.readLine()) != null) {
				ArrayList<Cell> tmp = new ArrayList<Cell>();
				var words = line.split("_");
				for (String s : words) {

					tmp.add(classification(s, x, y));
					y++;
				}
				x++;
				y = 0;
				grid.add(tmp);
			}
		}
	}

	private static Cell classification(String data, int x, int y) {
		return switch (data) {
		case "O" -> new Material("rock", x, y);
		case "F" -> new Material("flag", x, y);
		case "-" -> new Material("wall", x, y);
		case "*" -> new Element(data, x, y);
		case "M" -> new Element(data, x, y);
		case "X" -> new Element(data, x, y);
		case "baba" -> new Word(data, x, y);
		case "rock" -> new Word(data, x, y);
		case "flag" -> new Word(data, x, y);
		case "wall" -> new Word(data, x, y);
		case "is" -> new Operator(data, x, y);
		case "you" -> new Action(data, x, y);
		case "push" -> new Action(data, x, y);
		case "win" -> new Action(data, x, y);
		case "stop" -> new Action(data, x, y);
		default -> throw new IllegalArgumentException("Unexpected value: " + data);
		};
	}

	@Override
	public String toString() {
		
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < grid.size(); i++) {
			int size2 = grid.get(i).size();
			for (int j = 0; j < size2; j++) {
				 result.append(grid.get(i).get(j).toString()).append("\t");
			}
			result.append("\n");
		}
		return result.toString();

	}

}
