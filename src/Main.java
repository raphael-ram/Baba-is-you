import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		LevelConstruction start = new LevelConstruction(1);
		
		Grid g = start.startLevelGame();
		//display grid
		System.out.println(g.toString(start.getKeyWords()));

		 

	}

}
