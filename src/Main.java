public class Main {

	public static void main(String[] args) {
		LevelConstruction start = new LevelConstruction();
		
		Grid g = start.startLevelGame(1);
		//display grid
		System.out.println(g.toString(start.getKeyWords()));

		 

	}

}
