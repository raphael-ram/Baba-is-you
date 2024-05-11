package src.fr.uge.memory;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;



/**
 * The SimpleGameController class deals with the main game loop, including
 * retrieving raw user actions, sending them for analysis to the GameView and
 * GameData, and dealing with time events.
 * 
 * @author Raphaël && Sara
 */
public class SimpleGameController {
	/**
	 * Default constructor, which does basically nothing.
	 */
	public SimpleGameController() {
		
	}

	/**
	 * Goes once in the game loop, which consists in retrieving user actions,
	 * transmissing it to the GameView and GameData, and dealing with time events.
	 * 
	 * @param context {@code ApplicationContext} of the game.
	 * @param data    GameData of the game.
	 * @param view    GameView of the game.
	 * @return        True if the game must continue, False if it must stop.
	 */

	private static boolean gameLoop(ApplicationContext context, SimpleGameData data, SimpleGameView view) {
		var event = context.pollOrWaitEvent(10);
		if (event == null) {
			return true;
		}
		return switch (event) {
			case KeyboardEvent keyboardEvent -> {	
				var direction = switch(keyboardEvent.key()) {
					case UP -> Direction.UP;
					case DOWN -> Direction.DOWN;
					case LEFT -> Direction.LEFT;
					case RIGHT -> Direction.RIGHT;
					default ->null;
				};
				if(direction != null) {
					data.move(direction);
					SimpleGameView.draw(context, data, view);
				}
				yield true;
			}
			default -> false;
		};
	}



	/**
	 * Sets up the game, then launches the game loop.
	 * 
	 * @param context {@code ApplicationContext} of the game.
	 */

	private static void memoryGame(ApplicationContext context) {
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.width();
		var height = screenInfo.height();
		var margin = 0;

		var images = new ImageLoader();
		var data = new SimpleGameData(2, 10);
		
		try {
			data.loadFromTextFile("level.txt",images);
		}catch(IOException e) {
			System.out.println(e);
		}
		var view = SimpleGameView.initGameGraphics(margin, margin, (int) Math.min(width, height) - 2 * margin, data,
				images);
		SimpleGameView.draw(context, data, view);

		while (true) {
			if (!gameLoop(context, data, view)) {
				System.out.println("Thank you for quitting!");
				context.dispose();
				return;
			}
		}
	}


	/**
	 * Executable program.
	 *
	 * @param args Spurious arguments.
	 */

	public static void main(String[] args) {
		Application.run(Color.BLACK, SimpleGameController::memoryGame);
	}


}