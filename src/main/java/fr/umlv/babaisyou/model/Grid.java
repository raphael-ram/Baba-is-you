package fr.umlv.babaisyou.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Grid {

    private int nbLines;
    private int nbColumns;
    public ArrayList<ArrayList<LinkedList<Cell>>> grid;

    public Grid() {
        // Grid will be set when the level is loaded
    }

    /**
     * Verify if the move is possible
     *
     * @param d  direction
     * @param x1 coordinates X
     * @param y1 coordinates Y
     * @return true if the move is within the grid boundaries
     */
    public boolean possibleToMove(Direction d, int x1, int y1) {
        return !(x1 < 0 || x1 >= nbLines || y1 >= nbColumns || y1 < 0);
    }

    /**
     * Initialize the grid based on dynamic dimensions
     */
    private void settingGrid() {
        this.grid = new ArrayList<>(nbLines);
        for (int i = 0; i < nbLines; i++) {
            ArrayList<LinkedList<Cell>> tmp = new ArrayList<>(nbColumns);
            for (int j = 0; j < nbColumns; j++) {
                LinkedList<Cell> l = new LinkedList<>();
                l.addFirst(new Element("*", i, j));
                tmp.add(l);
            }
            this.grid.add(tmp);
        }
    }

    /**
     * Load the level data from a file and set the grid dimensions dynamically
     *
     * @param path the path to the level file
     * @throws IOException if an I/O error occurs
     */
    /**
     * Load the level data from a resource file and set the grid dimensions dynamically
     *
     * @param resourcePath the path to the level resource
     * @throws IOException if an I/O error occurs
     */
    public void launchingData(String resourcePath) throws IOException {
        Objects.requireNonNull(resourcePath);

        List<String> lines;
        try (var inputStream = Grid.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream, java.nio.charset.StandardCharsets.UTF_8))) {
                lines = reader.lines().filter(l -> !l.isBlank()).toList();
            }
        }

        // Determine the number of lines and columns
        this.nbLines = lines.size();
        if (nbLines > 0) {
            this.nbColumns = lines.get(0).split("_").length;
        } else {
            throw new IllegalArgumentException("Level file is empty.");
        }

        settingGrid();

        // Parse each line and populate the grid
        for (int row = 0; row < nbLines; row++) {
            String line = lines.get(row);
            String[] elements = line.split("_");

            // Ensure the number of elements matches the number of columns
            if (elements.length != nbColumns) {
                throw new IllegalArgumentException("Inconsistent number of columns at row " + row);
            }

            ArrayList<LinkedList<Cell>> gridRow = this.grid.get(row);
            for (int col = 0; col < elements.length; col++) {
                String element = elements[col].trim();
                LinkedList<Cell> cellList = gridRow.get(col);
                Cell modifiedElement = classification(element, row, col);
                cellList.addFirst(modifiedElement);
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
            case "T" -> new Material("tile", x, y);
            case "F" -> new Material("fan", x, y);
            case "B" -> new Material("box", x, y);
            default -> throw new IllegalArgumentException("Unexpected value: " + data);
        };
    }

    /**
     * To create Word element
     *
     * @param data string
     * @param x    coordinate X
     * @param y    coordinate Y
     * @return cell
     */
    private static Cell wordFabric(String data, int x, int y) {
        return switch (data) {
            case "baba", "rock", "flag", "wall", "lava", "skull", "water", "fan", "box" -> new Word(data, x, y);
            default -> throw new IllegalArgumentException("Unexpected value: " + data);
        };
    }

    /**
     * To create Action element
     *
     * @param data string
     * @param x    coordinate X
     * @param y    coordinate Y
     * @return cell
     */
    private static Cell actionFabric(String data, int x, int y) {
        return switch (data) {
            case "you", "push", "win", "stop", "melt", "defeat", "sink", "reverse", "pull", "hot" -> new Action(data, x, y);
            default -> throw new IllegalArgumentException("Unexpected value: " + data);
        };
    }

    /**
     * Parse the data and determine its category to transform it into a cell
     *
     * @param data string
     * @param x    coordinate X
     * @param y    coordinate Y
     * @return cell
     */
    private static Cell classification(String data, int x, int y) {
        var patternMaterial = Pattern.compile("(O|M|-|X|T|B|L|D|E|F)");
        var patternWord = Pattern.compile("(baba|rock|flag|wall|water|lava|skull|fan|box)");
        var patternAction = Pattern.compile("(push|you|win|stop|sink|defeat|melt|reverse|pull|hot)");

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

    // Getters for the grid dimensions
    public int getNbLines() {
        return nbLines;
    }

    public int getNbColumns() {
        return nbColumns;
    }
}
