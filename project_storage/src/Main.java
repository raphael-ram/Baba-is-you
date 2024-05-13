import java.io.IOException;
import java.nio.file.Path;

import projet.Model.Grid;

public class Main {

	public static void main(String[] args) throws IOException {

		Grid g = new Grid();
		Grid.launchingData(Path.of("src/external/level1.txt"));
		System.out.println(g.toString());

	}

}
