package projet.ControllerConsole;

import java.awt.Color;
import java.nio.file.Paths;
import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import project.View.GameView;
import projet.Model.Behaviour;
import projet.Model.Direction;
import projet.Model.Grid;
import projet.Model.ImageLoader;

public class GameController {

    private static boolean gameLoop(ApplicationContext context, Grid grid, GameView view, Behaviour behaviour) {
        var event = context.pollOrWaitEvent(1000 / 6);
        if (event == null) {
            if (view.getContext() != null) {
                view.repaint();
            }
            return true;
        } else {
            processEvent(event, grid, view, behaviour);
            return true;
        }
    }

    private static void processEvent(Object event, Grid grid, GameView view, Behaviour behaviour) {
        if (event instanceof KeyboardEvent keyboardEvent) {
            handleKeyboardEvent(keyboardEvent, grid, view, behaviour);
        }
    }

    private static void handleKeyboardEvent(KeyboardEvent keyboardEvent, Grid grid, GameView view, Behaviour behaviour) {
        if (keyboardEvent.action() == KeyboardEvent.Action.KEY_PRESSED) {
            var direction = getDirectionFromKey(keyboardEvent.key());
            if (direction != null) {
                moveBabas(direction, grid, view, behaviour);
            } else if (keyboardEvent.key() == KeyboardEvent.Key.ESCAPE) {
                System.out.println("Merci d'avoir joué !");
                view.getContext().dispose(); // Ferme le programme
                System.exit(0);
            }
        }
    }

    private static Direction getDirectionFromKey(KeyboardEvent.Key key) {
        return switch (key) {
            case UP -> Direction.Up;
            case DOWN -> Direction.Down;
            case LEFT -> Direction.Left;
            case RIGHT -> Direction.Right;
            default -> null;
        };
    }

    private static void moveBabas(Direction direction, Grid grid, GameView view, Behaviour behaviour) {
        behaviour.movementManager(direction);
        view.repaint();
    }

    private static void startGame(ApplicationContext context) {
        var loader = new ImageLoader();
        var grid = new Grid();
        
        // Charger les données du niveau avant de créer la vue pour assurer les dimensions valides
        try {
            grid.launchingData(Paths.get(selectedLevelPath));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        var view = new GameView(loader, grid, context);
        var behaviour = new Behaviour(grid);

        try {
            initializeGame(loader, grid, behaviour, view);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        runGameLoop(context, grid, view, behaviour);
    }

    private static void initializeGame(ImageLoader loader, Grid grid, Behaviour behaviour, GameView view) throws Exception {
        // Les données du niveau sont déjà chargées dans startGame()
        loader.loadImages(Paths.get("src/images"));
        behaviour.processing(); // Appliquer les règles initiales
        view.repaint();
    }

    private static void runGameLoop(ApplicationContext context, Grid grid, GameView view, Behaviour behaviour) {
        while (true) {
            if (!gameLoop(context, grid, view, behaviour)) {
                context.dispose();
                return;
            }

            // Check the play status after each loop iteration
            String playStatus = behaviour.play();
            if ("win".equals(playStatus)) {
                System.out.println("You Win! Congratulations!");
                context.dispose();
                return;
            } else if ("loose".equals(playStatus)) {
                System.out.println("You Lose! Try Again!");
                context.dispose();
                return;
            }
        }
    }

    private static String selectedLevelPath = "src/external/level_custom.txt";

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.println("=== Baba Is You (GUI) ===");
        System.out.println("Choose a level:");
        System.out.println("1. Level 1");
        System.out.println("2. Custom Level (level_custom.txt)");
        System.out.println("3. Enter custom path");
        System.out.print("> ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> selectedLevelPath = "src/external/level1.txt";
            case "2" -> selectedLevelPath = "src/external/level_custom.txt";
            case "3" -> {
                System.out.print("Enter path: ");
                selectedLevelPath = scanner.nextLine();
            }
            default -> System.out.println("Invalid choice, defaulting to Custom Level.");
        }

        Application.run(Color.BLACK, GameController::startGame);
    }
}
