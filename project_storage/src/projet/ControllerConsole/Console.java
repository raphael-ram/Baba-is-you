/**
 * 
 */
package projet.ControllerConsole;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import projet.Model.Cell;
import projet.Model.Direction;
import projet.Model.Grid;
import projet.Model.Rule;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;

/**
 * 
 */
public class Console {

	public void startGame() throws IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Bienvenue à toi !"
				+ "Baba is waiting for you...\nLes règles du jeu sont disponibles sur le site dédié et sont les mêmes. Cependant puis que tu veux jouer de la plus brave des manières c'est-à-dire en console, voici les explications pour ce faire.\n\n"
				+ "Concernant les déplacements, ils se feront avec les flèches directionnelles du clavier.\n\n"
				+ "Passons au choses sérieuses...Il existe 6 niveaux de jeu. Selon le chiffre que tu nous donneras nous lancerons le dit niveau.\n\n"
				+ "Attention en cas de défaite par une propriété, on te proposera de reprendre la partie. Sinon FIN DU GAME.\n\n"
				+ "Quand tu auras réussi un niveau, on te laisse choisir d'aller au niveau suivant ou de reprendre la partie parce que tu aimes juste le fun. Au terme de chaque partie. On te donnera ton temps de jeu.\n"
				+ "Cool non ?\n\n\n" + "Alors c'est parti pour renseigner tes informations !!!!!! ");
		System.out.println("3 manières pour démarrrer le jeu se présentent à vous. Entrez :\n"
				+ "1 -> si vous souhaitez entrer le nom du niveau qui vous interesse\n"
				+ "2 -> si vous souhaitez entrez le nom du fichier de niveau qui vous interesse\n"
				+ "3 -> saisissez votre regle de jeu");
		Grid g = null;
		var startingOpportunities = scanner.nextLine();
		System.out.print("C'est parti :) LE JEU PEUT COMMENCER\n");
		String level;
		switch (startingOpportunities) {
		case "1":
			System.out.print("Entrez level suivi d'un chiffre entre 1 et 6 pour commencer une partie\n----> ");
			level = scanner.nextLine();
			g = redirectionLevel(level);
			break;
		case "2":
			System.out.print("Entrez le chemin du fichier de niveau de jeu pour commencer une partie\n----> ");
			level = scanner.nextLine();
			g = runningGame(Path.of(level));
			break;
		case "3":
			System.out.println("undefined for the moment");
			break;
		default:
			System.out.println("Donnée saisie incorrecte. Relancez le programme pour recommencer. A la prochaine !");
		}

		System.out.println("IBRAHIIIIIM");
		// ArrayList <Boolean> resunlt = new ArrayList<>();
		
		Rule rule = new Rule(g);

		boolean play = false;
		System.out.println(g.toString());

		while (play == false) {
			rule.researchRule();
			String direction = scanner.nextLine();
			
			 List<Cell> cells = Grid.grid.stream().flatMap(List::stream).filter(r -> r.isPawn()).toList();

   // Perform movement based on the chosen direction
			 ArrayList<Boolean> results = null;
			if (direction.equals("u")) {
				results = performMovement(Direction.Up, cells);
			} else if (direction.equals("d")) {
				results = performMovement(Direction.Down, cells);
			} else if (direction.equals("l")) {
				results = performMovement(Direction.Left, cells);
			} else if (direction.equals("r")) {
				results = performMovement(Direction.Right, cells);
			}
			
			System.out.println(g.toString());
			System.out.println("OKKIII " + results);
			if(results != null && results.contains(true)) {
				play = true;
				System.out.println("UOU  AREEE");

			}
			

		}

	}
	
	public static ArrayList<Boolean> performMovement(Direction direction, List<Cell> cells) {
    ArrayList<Boolean> results = new ArrayList<>();

    for (Cell cell : cells) {
        boolean result = Grid.movement(direction, cell);
        results.add(result);
    }

    return results;
}

	public Grid runningGame(Path path) throws IOException {
		Objects.requireNonNull(path);
		if (Files.exists(path) == false)
			throw new IllegalArgumentException("Fichier incorrect");

		System.out.println("OKKO");
		Grid g = new Grid();
		Grid.launchingData(Path.of("src/external/level1.txt"));
		return g;
	}

	public Grid redirectionLevel(String level) throws IOException {
		return switch (level) {
		case "level 1" -> runningGame(Path.of("src/external/level1.txt"));
		case "level 2" -> runningGame(Path.of("src/external/level1.txt"));
		case "level 3" -> runningGame(Path.of("src/external/level1.txt"));
		case "level 4" -> runningGame(Path.of("src/external/level1.txt"));
		case "level 5" -> runningGame(Path.of("src/external/level1.txt"));
		case "level 6" -> runningGame(Path.of("src/external/level1.txt"));
		default -> throw new IllegalArgumentException("Vous avez saisi un niveau de jeu incorrect. Au revoir !");
		};
	}

}
