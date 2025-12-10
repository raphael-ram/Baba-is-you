import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

import projet.ControllerConsole.Console;
import projet.Model.Behaviour;
import projet.Model.Direction;
import projet.Model.Grid;

public class Main {

	public static void main(String[] args) throws IOException {
		Console console = new Console();
		console.startGame();
	}

}
