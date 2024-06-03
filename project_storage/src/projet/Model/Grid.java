package projet.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class Grid {

	/***
	 * This class is about the grid of the game, we should be able to set a grid
	 * according to the level The toString is particular ()
	 */

	private final static int nbLines = 20;
	private final static int nbColumns = 20;
	public ArrayList<ArrayList<LinkedList<Cell>>> grid; 
	
	

	public Grid() {
		this.grid = new ArrayList<ArrayList<LinkedList<Cell>>>();
	}
		
	
	

	/**
	 * Verify if the move is possible
	 * 
	 * @param d  direction
	 * @param x1 coordinates X
	 * @param y1 coordinates Y
	 * @return
	 */

	public boolean possibleToMove(Direction d, int x1, int y1) {
		return !(x1 < 0 || x1 > (nbLines - 1) || y1 > (nbColumns - 1) || y1 < 0);
	}

	public  void settingGrid() {

		for (int i = 0; i < 20; i++) {
			ArrayList<LinkedList<Cell>> tmp = new ArrayList<>();
			for (int j = 0; j < 20; j++) {
				LinkedList<Cell> l = new LinkedList<>();
				l.addFirst(new Element("*", i, j));
				tmp.add(l);
			}
			this.grid.add(tmp);
		}
	}

	public void launchingData(Path path) throws IOException {
		Objects.requireNonNull(path);
		settingGrid();
		try (var reader = Files.newBufferedReader(path)) {
			String line;
			var count = 0;
			while ((line = reader.readLine()) != null) {
				var words = line.split("_");

				ArrayList<LinkedList<Cell>> row = this.grid.get(count);
				for (int i = 0; i < words.length; i++) {

					LinkedList<Cell> cellList = row.get(i);
					Cell modifiedElement = classification(words[i], cellList.getFirst().getPositionX(),
							cellList.getFirst().getPositionY());
					cellList.addFirst(modifiedElement);
				}
				count ++;
			}
			

		}
	}

	/**
	 * To create Material element
	 * 
	 * @param data string
	 * @param x    coordinate X
	 * @param y    coordinate Y
	 * @return cell
	 */
	private static Cell materialFabric(String data, int x, int y) {
		return switch (data) {
		case "O" -> new Material("rock", x, y);
		case "-" -> new Material("wall", x, y);
		case "X" -> new Material("flag", x, y);
		case "M" -> new Material("baba", x, y);
		case "L" -> new Material("lava", x, y);
		case "D" -> new Material("skull", x, y);
		case "E" -> new Material("water", x, y);
		case "F" -> new Material("fan", x, y);
		default -> throw new IllegalArgumentException("Unexpected value: " + data);
		};
	}

	/**
	 * To create Word element
	 * 
	 * @param data string
	 * @param x    coordinate X
	 * @param y    coordinate Y
	 * @return
	 */
	private static Cell wordFabric(String data, int x, int y) {
		return switch (data) {
		case "baba" -> new Word(data, x, y);
		case "rock" -> new Word(data, x, y);
		case "flag" -> new Word(data, x, y);
		case "wall" -> new Word(data, x, y);
		case "lava" -> new Word(data, x, y);
		case "skull" -> new Word(data, x, y);
		case "water" -> new Word(data, x, y);
		case "fan" -> new Word(data, x, y);
		default -> throw new IllegalArgumentException("Unexpected value: " + data);
		};
	}

	/**
	 * To create Action element
	 * 
	 * @param data string
	 * @param x    coordinate X
	 * @param y    coordinate Y
	 * @return
	 */
	private static Cell actionFabric(String data, int x, int y) {
		return switch (data) {
		case "you" -> new Action(data, x, y);
		case "push" -> new Action(data, x, y);
		case "win" -> new Action(data, x, y);
		case "stop" -> new Action(data, x, y);
		case "melt" -> new Action(data, x, y);
		case "defeat" -> new Action(data, x, y);
		case "sink" -> new Action(data, x, y);
		case "reverse" -> new Action(data, x, y);
		default -> throw new IllegalArgumentException("Unexpected value: " + data);
		};
	}

	/**
	 * Parse the data and determine its category for to transform it as a cell
	 * 
	 * @param data string
	 * @param x    coordinate X
	 * @param y    coordinate Y
	 * @return
	 */
	private static Cell classification(String data, int x, int y) {
		var patternMaterial = Pattern.compile("(O|M|-|X)");
		var patternWord = Pattern.compile("(baba|rock|flag|wall|water|lava|skull|fan)");
		var patternAction = Pattern.compile("(push|you|win|stop|sink|defeat|melt|reverse)");

		if (patternMaterial.matcher(data).matches())
			return materialFabric(data, x, y);
		if (patternWord.matcher(data).matches())
			return wordFabric(data, x, y);
		if (patternAction.matcher(data).matches())
			return actionFabric(data, x, y);
		if (data.equals("*"))
			return new Element(data, x, y);
		if (data.equals("is"))
			return new Operator(data, x, y);
		throw new IllegalArgumentException("Unexpected value: " + data);
	}

	/**
	 * To String method
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (ArrayList<LinkedList<Cell>> row : this.grid) {
			for (LinkedList<Cell> cellList : row) {
				if (!cellList.isEmpty()) {
					result.append(cellList.getFirst()).append("\t");
				}
			}
			result.append("\n");
		}
		return result.toString();
	}

}
