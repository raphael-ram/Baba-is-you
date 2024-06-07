package project.View;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import com.github.forax.zen.ApplicationContext;
import projet.Model.Cell;
import projet.Model.Grid;
import projet.Model.ImageLoader;

public class GameView {
    private final ImageLoader loader;
    private final Grid grid;
    private final ApplicationContext context;

    private final int cellSize; // Taille des cellules
    private final int xOffset; // Décalage horizontal pour centrer la grille
    private final int yOffset; // Décalage vertical pour centrer la grille

    public GameView(ImageLoader loader, Grid grid, ApplicationContext context) {
        this.loader = loader;
        this.grid = grid;
        this.context = context;

        // Vérifier les dimensions de la grille
        int numColumns = grid.getNbColumns();
        int numLines = grid.getNbLines();

        if (numColumns <= 0 || numLines <= 0) {
            throw new IllegalArgumentException("La grille doit avoir des dimensions valides (non nulles).");
        }

        // Calculer la taille des cellules et les décalages une fois
        int screenWidth = context.getScreenInfo().width();
        int screenHeight = context.getScreenInfo().height();
        int cellWidth = screenWidth / numColumns;
        int cellHeight = screenHeight / numLines;
        this.cellSize = Math.min(cellWidth, cellHeight);

        this.xOffset = (screenWidth - cellSize * numColumns) / 2;
        this.yOffset = (screenHeight - cellSize * numLines) / 2;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void repaint() {
        context.renderFrame(this::drawGrid);
    }

    private void drawGrid(Graphics2D g) {
        // Remplir le fond de l'écran en noir
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());

        // Dessiner chaque cellule
        for (int i = 0; i < grid.getNbLines(); i++) {
            for (int j = 0; j < grid.getNbColumns(); j++) {
                drawCell(g, i, j);
            }
        }
    }

    private void drawImage(Graphics2D g, BufferedImage image, int x, int y, int width, int height) {
        // Calculer l'échelle pour adapter l'image à la taille de la cellule
        var scale = Math.min((float) width / image.getWidth(), (float) height / image.getHeight());
        var transform = new AffineTransform();
        transform.translate(x, y);
        transform.scale(scale, scale);

        g.drawImage(image, transform, null);
    }

    private void drawCell(Graphics2D g, int row, int col) {
        int xPosition = xOffset + col * cellSize;
        int yPosition = yOffset + row * cellSize;

        // Remplir la cellule avec du noir pour effacer les anciennes images
        g.setColor(Color.BLACK);
        g.fillRect(xPosition, yPosition, cellSize, cellSize);

        List<Cell> cellList = grid.grid.get(row).get(col);
        if (!cellList.isEmpty()) {
            // Dessiner tous les éléments dans la cellule, du haut vers le bas
            for (int i = cellList.size() - 1; i >= 0; i--) {
                Cell cell = cellList.get(i);
                BufferedImage image = loader.getNextImage(cell.property(), cell);
                if (image != null) {
                    drawImage(g, image, xPosition, yPosition, cellSize, cellSize);
                }
            }
        }
    }
}
