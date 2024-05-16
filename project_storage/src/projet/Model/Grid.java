package projet.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;

public class Grid {

	/***
	 * This class is about the grid of the game, we should be able to set a grid
	 * according to the level The toString is particular ()
	 */

	private final int nbLines = 20;
	private final int nbColumns = 20;
	public static ArrayList<ArrayList<Cell>> grid = new ArrayList<ArrayList<Cell>>();

	/**
	 * @brief Verify if the move is possible
	 * @param d  direction
	 * @param x1 coordinates X
	 * @param y1 coordinates Y
	 * @return
	 */
	public boolean possibleToMove(Direction d, int x1, int y1) {
		return !(x1 < 0 || x1 > (nbLines - 1) || y1 > (nbColumns - 1) || y1 < 0);
	}

	public void movement(Direction d, Cell cell) {
		var cellX = cell.getPositionX();// initial coordinates XY of the cell and his potential next coordinates
		var cellY = cell.getPositionY();
		var moveX = d.x + cell.getPositionX();
		var moveY = d.y + cell.getPositionY();

		if (possibleToMove(d, moveX, moveY)) {// check if movement is possible
			Cell tmp = grid.get(moveX).get(moveY);
			if (tmp.isStop() == false) {
				if (tmp.isPushable() == false) { // if it is an element it is a simple push
					exchange(tmp, cell, moveX, moveY, cellX, cellY);
				} else {
					if (possibleToMove(d, moveX + d.x, moveX + d.x)) {// check element forward to make a double
						Cell nextTmp = grid.get(moveX + d.x).get(moveY + d.y);
						forward(nextTmp, tmp, cell, moveX + d.x, moveY + d.y, moveX, moveY, cellX, cellY);
					}
				}
			} else if (tmp.isWin() == true) {System.out.println("Partie gagnée");}
			System.out.println("Pas de mouvement possible -> element stop");
		} else { System.out.println("Pas de mouvement possible");}
	}

	/**
	 * 
	 * @param nextTmp the cell heading the trio (depending on the direction we want
	 *                to deplace)
	 * @param tmp     the mid cell
	 * @param cell    the cell behind in trio
	 * @param x1      coordinate X of nextTmp
	 * @param y1      coordinate Y of nextTmp
	 * @param x2      coordinate X of tmp
	 * @param y2      coordinate Y of tmp
	 * @param x3      coordinate X of cell
	 * @param y3      coordinate Y of cell
	 */
	public void forward(Cell nextTmp, Cell tmp, Cell cell, int x1, int y1, int x2, int y2, int x3, int y3) {

		if (nextTmp.isPushable() == false) {
			exchange(nextTmp, tmp, x1, y1, x2, y2);
			exchange(nextTmp, cell, x2, y2, x3, y3);
		}
	}

	/**
	 * @brief Exchange cells
	 * @param tmp     cell to switch with
	 * @param initial cell
	 * @param moveX   coordinate X of tmp
	 * @param moveY   coordinate Y of tmp
	 * @param cellX   coordinate X of cell
	 * @param cellY   coordinate y of cell
	 */
	public void exchange(Cell tmp, Cell cell, int moveX, int moveY, int cellX, int cellY) {
		if (tmp.isWin()) {
			System.out.println("JEU gagné");
			grid.get(moveX).set(moveY, cell);
			grid.get(cellX).set(cellY, new Element("*", cellX, cellY));
		} else {
			cell.update_position(moveX, moveY);
			tmp.update_position(cellX, cellY);
			grid.get(moveX).set(moveY, cell);
			grid.get(cellX).set(cellY, tmp);
		}

	}

	public static void launchingData(Path path) throws IOException {
		Objects.requireNonNull(path);
		try (var reader = Files.newBufferedReader(path)) { // read the provided file
			String line;
			var x = 0;
			var y = 0;
			while ((line = reader.readLine()) != null) {
				var tmp = new ArrayList<Cell>();
				var words = line.split("_");
				for (String s : words) {
					tmp.add(classification(s, x, y));// parse each string
					y++;
				}
				x++;
				y = 0;
				grid.add(tmp);
			}
		}
	}

	/**
	 * @brief To create Material element
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
		default -> throw new IllegalArgumentException("Unexpected value: " + data);
		};
	}

	/**
	 * @brief Parse the data and determine its category for to transform it as a
	 *        cell
	 * @param data string
	 * @param x    coordinate X
	 * @param y    coordinate Y
	 * @return
	 */
	private static Cell classification(String data, int x, int y) {
		var patternMaterial = Pattern.compile("(O|M|-|X)");
		var patternWord = Pattern.compile("(baba|rock|flag|wall|water|lava|skull)");
		var patternAction = Pattern.compile("(push|you|win|stop|sink|defeat|melt)");

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
		var result = new StringBuilder();
		for (int i = 0; i < grid.size(); i++) {
			var size2 = grid.get(i).size();
			for (int j = 0; j < size2; j++) {
				result.append(grid.get(i).get(j).toString()).append("\t");
			}
			result.append("\n");
		}
		return result.toString();

	}

}

//private static Cell classification(String data, int x, int y) {
//var pattern = Pattern.compile("");
//var matcher = pattern.matcher("aaaa");
//return switch (data) {
//	
//case "O" -> new Material("rock", x, y);
//case "F" -> new Material("flag", x, y);
//case "-" -> new Material("wall", x, y);
//case "*" -> new Element(data, x, y);
//case "M" -> new Element(data, x, y);
//case "X" -> new Element(data, x, y);
//case "baba" -> new Word(data, x, y);
//case "rock" -> new Word(data, x, y);
//case "flag" -> new Word(data, x, y);
//case "wall" -> new Word(data, x, y);
//case "is" -> new Operator(data, x, y);
//case "you" -> new Action(data, x, y);
//case "push" -> new Action(data, x, y);
//case "win" -> new Action(data, x, y);
//case "stop" -> new Action(data, x, y);
//default -> throw new IllegalArgumentException("Unexpected value: " + data);
//};
//}
