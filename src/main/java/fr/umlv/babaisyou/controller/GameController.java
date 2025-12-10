package fr.umlv.babaisyou.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;
import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import fr.umlv.babaisyou.view.GameView;
import fr.umlv.babaisyou.model.Behaviour;
import fr.umlv.babaisyou.model.Direction;
import fr.umlv.babaisyou.model.Grid;
import fr.umlv.babaisyou.model.ImageLoader;

public class GameController {

    private enum State {
        MENU, GAME
    }

    private static State state = State.MENU;
    private static GameView view;
    private static Grid grid;
    private static Behaviour behaviour;
    private static ImageLoader loader;

    public static void main(String[] args) {
        Application.run(Color.BLACK, GameController::startGame);
    }

    private static void startGame(ApplicationContext context) {
        loader = new ImageLoader();
        try {
            loader.loadImages();
        } catch (IOException e) {
            System.err.println("Failed to load images: " + e.getMessage());
            return;
        }

        while (true) {
            var event = context.pollOrWaitEvent(1000 / 6); // Reduced FPS to fix animation speed
            if (event != null) {
                processEvent(event, context);
            }
            render(context);
        }
    }

    private static void processEvent(Object event, ApplicationContext context) {
        if (event instanceof KeyboardEvent keyboardEvent) {
            if (keyboardEvent.action() == KeyboardEvent.Action.KEY_PRESSED) {
                if (state == State.MENU) {
                    handleMenuKeys(keyboardEvent, context);
                } else {
                    handleGameKeys(keyboardEvent, context);
                }
            }
        }
    }

    private static void handleMenuKeys(KeyboardEvent event, ApplicationContext context) {
        String levelPath = null;
        switch (event.key()) {
            case A -> levelPath = "/levels/level1.txt";
            case B -> levelPath = "/levels/level2.txt";
            case C -> levelPath = "/levels/level3.txt";
            case D -> levelPath = "/levels/level4.txt";
            case E -> levelPath = "/levels/level5.txt";
            case F -> levelPath = "/levels/level6.txt";
            case G -> levelPath = "/levels/level_custom.txt";
            case ESCAPE -> {
                context.dispose();
                System.exit(0);
            }
        }

        if (levelPath != null) {
            loadLevel(levelPath, context);
        }
    }

    private static void handleGameKeys(KeyboardEvent event, ApplicationContext context) {
        if (event.key() == KeyboardEvent.Key.ESCAPE) {
            state = State.MENU;
            return;
        }

        Direction direction = switch (event.key()) {
            case UP -> Direction.Up;
            case DOWN -> Direction.Down;
            case LEFT -> Direction.Left;
            case RIGHT -> Direction.Right;
            default -> null;
        };

        if (direction != null) {
            behaviour.movementManager(direction);
            checkGameStatus();
        }
    }

    private static void checkGameStatus() {
        String playStatus = behaviour.play();
        if ("win".equals(playStatus)) {
            System.out.println("You Win!");
            state = State.MENU; // Go back to menu on win
        } else if ("loose".equals(playStatus)) {
            System.out.println("You Lose!");
            state = State.MENU;
        }
    }

    private static void loadLevel(String path, ApplicationContext context) {
        grid = new Grid();
        try {
            grid.launchingData(path);
            view = new GameView(loader, grid, context);
            behaviour = new Behaviour(grid);
            behaviour.processing();
            state = State.GAME;
        } catch (Exception e) {
            System.err.println("Error loading level " + path + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void render(ApplicationContext context) {
        context.renderFrame(g -> {
            if (state == State.MENU) {
                drawMenu(g, context);
            } else if (view != null) {
                view.draw(g);
            }
        });
    }

    private static void drawMenu(Graphics2D g, ApplicationContext context) {
        int width = context.getScreenInfo().width();
        int height = context.getScreenInfo().height();

        // Background
        g.setColor(new Color(30, 30, 30)); // Dark gray background
        g.fillRect(0, 0, width, height);

        // Title
        g.setColor(new Color(255, 69, 0)); // Orange-Red title
        g.setFont(new Font("SansSerif", Font.BOLD, 48));
        String title = "BABA IS YOU";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (width - titleWidth) / 2, 100);

        // Menu Items
        g.setFont(new Font("SansSerif", Font.PLAIN, 24));
        g.setColor(Color.WHITE);

        String[] menuItems = {
            "A. Level 1",
            "B. Level 2",
            "C. Level 3 (Melt/Hot)",
            "D. Level 4 (Sink)",
            "E. Level 5 (Defeat)",
            "F. Level 6 (Pull)",
            "G. Custom Level",
            "ESC. Quit"
        };

        int startY = 180;
        int lineHeight = 35;

        for (int i = 0; i < menuItems.length; i++) {
            String item = menuItems[i];
            int itemWidth = g.getFontMetrics().stringWidth(item);
            
            // Highlight "Quit" in red
            if (item.startsWith("ESC")) {
                g.setColor(new Color(220, 20, 60));
            } else {
                g.setColor(Color.WHITE);
            }
            
            g.drawString(item, (width - itemWidth) / 2, startY + (i * lineHeight));
        }
        
        // Footer
        g.setFont(new Font("SansSerif", Font.ITALIC, 14));
        g.setColor(Color.GRAY);
        String footer = "Press key to select level";
        int footerWidth = g.getFontMetrics().stringWidth(footer);
        g.drawString(footer, (width - footerWidth) / 2, height - 30);
    }
}
