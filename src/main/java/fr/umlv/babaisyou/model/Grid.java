package fr.umlv.babaisyou.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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

        // Determine the number of lines and columns (space-separated single-char tokens)
        this.nbLines = lines.size();
        if (nbLines > 0) {
            this.nbColumns = lines.get(0).trim().split("\\s+").length;
        } else {
            throw new IllegalArgumentException("Level file is empty.");
        }

        settingGrid();

        // Parse each line and populate the grid
        for (int row = 0; row < nbLines; row++) {
            String line = lines.get(row);
            String[] tokens = line.trim().split("\\s+");

            ArrayList<LinkedList<Cell>> gridRow = this.grid.get(row);
            for (int col = 0; col < nbColumns; col++) {
                String token = col < tokens.length ? tokens[col].trim() : "-";
                LinkedList<Cell> cellList = gridRow.get(col);
                Cell modifiedElement = classification(token, row, col);
                cellList.addFirst(modifiedElement);
            }
        }
    }

    /**
     * Parse a single-character BabaIsYou token and return the matching Cell.
     * Format: space-separated tokens, uppercase = entity, lowercase = word/property.
     *
     * Entities  (uppercase) : B=baba F=flag W=wall A=water S=skull L=lava R=rock T=tile N=smiley
     * Noun words (lowercase): b=baba f=flag w=wall a=water s=skull l=lava r=rock n=smiley
     * Operator              : i=is
     * Properties            : y=you v=win t=stop p=push m=melt h=hot d=defeat k=sink u=jump
     * Empty                 : - (or anything else)
     */
    /**
     * Parse a single-character BabaIsYou token and return the matching Cell.
     * Format: space-separated tokens, uppercase = entity, lowercase = word/property.
     *
     * Entities  (uppercase) : B=baba F=flag W=wall A=water S=skull L=lava R=rock T=tile N=smiley
     * Noun words (lowercase): b=baba f=flag w=wall a=water s=skull l=lava r=rock n=smiley
     * Operator              : i=is
     * Properties            : y=you v=win t=stop p=push m=melt h=hot d=defeat k=sink u=jump
     * Empty                 : - (or anything else)
     */
    private static Cell classification(String token, int x, int y) {
        return switch (token) {
            // --- Entities → Material ---
            case "B" -> new Material("baba",   x, y);
            case "F" -> new Material("flag",   x, y);
            case "W" -> new Material("wall",   x, y);
            case "A" -> new Material("water",  x, y);
            case "S" -> new Material("skull",  x, y);
            case "L" -> new Material("lava",   x, y);
            case "R" -> new Material("rock",   x, y);
            case "T" -> new Material("tile",   x, y);
            case "N" -> new Material("smiley", x, y);
            // --- Noun words → Word ---
            case "b" -> new Word("baba",   x, y);
            case "f" -> new Word("flag",   x, y);
            case "w" -> new Word("wall",   x, y);
            case "a" -> new Word("water",  x, y);
            case "s" -> new Word("skull",  x, y);
            case "l" -> new Word("lava",   x, y);
            case "r" -> new Word("rock",   x, y);
            case "n" -> new Word("smiley", x, y);
            // --- Operator ---
            case "i" -> new Operator("is", x, y);
            // --- Properties → Action ---
            case "y" -> new Action("you",    x, y);
            case "v" -> new Action("win",    x, y);
            case "t" -> new Action("stop",   x, y);
            case "p" -> new Action("push",   x, y);
            case "m" -> new Action("melt",   x, y);
            case "h" -> new Action("hot",    x, y);
            case "d" -> new Action("defeat", x, y);
            case "k" -> new Action("sink",   x, y);
            case "u" -> new Action("jump",   x, y);
            // --- Empty / background ---
            default  -> new Element("*", x, y);
        };
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
