import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

import projet.Model.Direction;
import projet.Model.Grid;

public class Main {

	public static void main(String[] args) throws IOException {

		Grid g = new Grid();
		Grid.launchingData(Path.of("src/external/level1.txt"));
		System.out.println(g.toString());
		Scanner monObj = new Scanner(System.in);
		System.out.println("Choisiez le direction de deplacement: U(up), D(down), L(left), et R(right).");
    String direction = monObj.nextLine();
    
    if (direction.equals("u")) {
    	g.exchange(Direction.Up, Grid.grid.get(2).get(1));
    }
    System.out.println("AFTER");
    System.out.println(g.toString());

	}

}
