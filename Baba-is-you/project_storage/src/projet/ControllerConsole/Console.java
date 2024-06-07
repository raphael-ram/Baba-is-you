/**
 * 
 */
package projet.ControllerConsole;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;

import projet.Model.Behaviour;
import projet.Model.Direction;
import projet.Model.Grid;



/**
 * 
 */
public class Console {


	public void startGame() throws IOException {
		
		try (Scanner scanner = new Scanner(System.in)) {
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
			System.out.print("C'est parti :) LE JEU PEUT COMMENCER\n");
			
			Grid g  = null;
			
			
			var startingOpportunities = scanner.nextLine();
			
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
				System.out.println("'Bout to start");
				System.out.println(g.toString());

				Behaviour behaviour = new Behaviour(g);

				behaviour.processing();
				String play = behaviour.play();

				while (play.equals("continue")) {

					String direction = scanner.nextLine();
					// Perform movement based on the chosen direction
					if (direction.equals("u")) {
						behaviour.movementManager(Direction.Up);
					} else if (direction.equals("d")) {
						behaviour.movementManager(Direction.Down);
					} else if (direction.equals("l")) {
						behaviour.movementManager(Direction.Left);
					} else if (direction.equals("r")) {
						behaviour.movementManager(Direction.Right);
					}
					System.out.println(g.toString());
					if (behaviour.play().equals("loose")) {
						System.out.println("PERDUUU !");
						break;
					} else if (behaviour.play().equals("win")) {
						System.out.println("GAGNEEE !");
						break;
					}

				}
			}
		}

	


	public Grid runningGame(Path path) throws IOException {
		Objects.requireNonNull(path);
		if (Files.exists(path) == false)
			throw new IllegalArgumentException("Fichier incorrect");

		Grid g = new Grid();
		g.launchingData(path);
		return g;
	}

	public Grid redirectionLevel(String level) throws IOException {
		return switch (level) {
		case "level 1" -> runningGame(Path.of("project_storage/src/external/level1.txt"));
		case "level 2" -> runningGame(Path.of("project_storage/src/external/src/external/level2.txt"));
		case "level 3" -> runningGame(Path.of("project_storage/src/external/src/external/level3.txt"));
		case "level 4" -> runningGame(Path.of("project_storage/src/external/src/external/level4.txt"));
		case "level 5" -> runningGame(Path.of("project_storage/src/external/src/external/level5.txt"));
		case "level 6" -> runningGame(Path.of("project_storage/src/external/src/external/level6.txt"));
		default -> throw new IllegalArgumentException("Vous avez saisi un niveau de jeu incorrect. Au revoir !");
		};
	}

}
