import java.io.IOException;

import projet.ControllerConsole.Console;

public class Main {

	public static void main(String[] args) throws IOException {
		Console console = new Console();
		console.startGame();

		//Grid g = new Grid();
		
//		g.launchingData(Path.of("src/external/level1.txt"));
//		Behaviour behaviour = new Behaviour(g);
//		behaviour.process();
		
		
		//System.out.println(g.toString());
//		Scanner monObj = new Scanner(System.in);
//		System.out.println("Choisiez le direction de deplacement: U(up), D(down), L(left), et R(right).");
//		String direction = monObj.nextLine();
//		Cell pown = null;
//		Rule rule = new Rule(g);
//		rule.rulesApplication();
		
		//Behaviour behaviour = new Behaviour(g);
		
		
		
		
		
		//behaviour.process();
		
		
		
		
		
		
		
//		System.out.println(g.grid.stream().flatMap(List::stream).filter(r -> r.isPawn()).toList());
//		ArrayList<Cell> cep = new ArrayList<>();
//		Collections.addAll(cep, Grid.grid.get(18).get(2), Grid.grid.get(1).get(16), Grid.grid.get(1).get(17));
//		rule.pushRule(cep);
//    
//    for (int i = 0; i < g.grid.size(); i++) {
//    	if (g.grid.get(i).stream().filter(r -> r.isMaterial()).filter(r -> r.property().equals("baba"))
//						.findFirst().orElse(null) != null)
//				pown = g.grid.get(i).stream().filter(r -> r.isMaterial()).filter(r -> r.property().equals("baba"))
//						.findFirst().orElse(null);
//			}

//		if (direction.equals("u")) {
//			g.movement(Direction.Down, Grid.grid.get(7).get(14));
//			g.movement(Direction.Down, Grid.grid.get(8).get(14));
//			g.movement(Direction.Down, Grid.grid.get(9).get(14));
//			g.movement(Direction.Down, Grid.grid.get(10).get(14));
//			g.movement(Direction.Down, Grid.grid.get(11).get(14));
//			g.movement(Direction.Down, Grid.grid.get(10).get(14));
//			g.movement(Direction.Down, Grid.grid.get(9).get(14));
//			g.movement(Direction.Down, Grid.grid.get(10).get(14));

			// g.movement(Direction.Left, Grid.grid.get(7).get(15));
			// g.movement(Direction.Up, Grid.grid.get(1).get(2));
//    	g.movement(Direction.Down, Grid.grid.get(1).get(16));
//    	g.movement(Direction.Left, Grid.grid.get(2).get(16));
//    	
//    	g.movement(Direction.Down, Grid.grid.get(1).get(17));
//    	g.movement(Direction.Down, Grid.grid.get(2).get(17));
//    	g.movement(Direction.Left, Grid.grid.get(3).get(17));
////    	
//    	g.movement(Direction.Left, Grid.grid.get(3).get(16));
//    	g.movement(Direction.Up, Grid.grid.get(3).get(15));
//    	g.movement(Direction.Up, pown);
//		}
//		System.out.println("AFTER");
//		System.out.println(g.toString());
//		// direction = monObj.nextLine();
//
////    if (direction.equals("u")) {
////    	g.movement(Direction.Left, Grid.grid.get(0).get(2));
////    }
////    System.out.println("AFTERPP");
////    System.out.println(g.toString());
//
////    Rule r = new Rule(g);
////    r.researchRule();

	}

}
