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

    private static final String[] LEVEL_PATHS = {
        "/levels/level0.txt",
        "/levels/level1.txt",
        "/levels/level2.txt",
        "/levels/level3.txt",
        "/levels/level4.txt",
        "/levels/level5.txt",
        "/levels/level6.txt",
        "/levels/level7.txt",
        "/levels/level8.txt",
    };

    private static State state = State.MENU;
    private static GameView view;
    private static Grid grid;
    private static Behaviour behaviour;
    private static ImageLoader loader;
    private static ApplicationContext appContext;
    private static int currentLevelIndex = 0;

    public static void main(String[] args) {
        Application.run(Color.BLACK, GameController::startGame);
    }

    private static void startGame(ApplicationContext context) {
        appContext = context;
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
        int levelIndex = -1;
        switch (event.key()) {
            case A -> levelIndex = 0;
            case B -> levelIndex = 1;
            case C -> levelIndex = 2;
            case D -> levelIndex = 3;
            case E -> levelIndex = 4;
            case F -> levelIndex = 5;
            case G -> levelIndex = 6;
            case H -> levelIndex = 7;
            case I -> levelIndex = 8;
            case ESCAPE -> {
                context.dispose();
                System.exit(0);
            }
        }

        if (levelIndex >= 0 && levelIndex < LEVEL_PATHS.length) {
            currentLevelIndex = levelIndex;
            loadLevel(LEVEL_PATHS[currentLevelIndex], context);
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
            int nextIndex = currentLevelIndex + 1;
            if (nextIndex < LEVEL_PATHS.length) {
                currentLevelIndex = nextIndex;
                loadLevel(LEVEL_PATHS[currentLevelIndex], appContext);
            } else {
                // All levels completed — go back to menu
                System.out.println("Congratulations! You completed all levels!");
                state = State.MENU;
            }
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
            "C. Level 3",
            "D. Level 4",
            "E. Level 5",
            "F. Level 6",
            "G. Level 7",
            "H. Level 8",
            "I. Level 9  [HARDEST]",
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
