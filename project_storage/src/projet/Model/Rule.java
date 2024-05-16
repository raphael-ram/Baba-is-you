/**
 * 
 */
package projet.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 
 */
public class Rule {
	private final Grid g;

	public Rule(Grid g) {
		this.g = g;
	}

	public void researchRule() {
		List<Direction> directions = List.of(Direction.Right, Direction.Down);
		for (int i = 0; i < g.grid.size(); i++) {
			for (int j = 1; j < g.grid.get(i).size(); j++) {
				if (g.grid.get(i).get(j).isProperty()) {
					// Cell prev = g.grid.get(i).get(j - 1);
					for (Direction d : directions) {
						if (verification(g.grid.get(i).get(j), d) != null) {
							System.out.println("research");
							youRule(verification(g.grid.get(i).get(j), d));
						}
					}
				}
			}
		}
	}

	private void desactivateYou() {
		Consumer<Cell> removeElement = s -> {
			s.setBaba(false);
		};
		for (int i = 0; i < g.grid.size(); i++) {
			g.grid.get(i).stream().forEach(removeElement);
		}
	}

	private void youRule(ArrayList<Cell> rule) {
		desactivateYou();
		Consumer<Cell> newPown = s -> {
			s.setBaba(true);
		};
		if (rule.stream().anyMatch(r -> r.property().equals("you"))) { // array containing you as action
			Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null); // get word of this
																																																// array
			if (c != null) {
				for (int i = 0; i < g.grid.size(); i++) {
					g.grid.get(i).stream().filter(r -> r.isMaterial()).filter(r -> r.property().equals(c.property()))
							.forEach(newPown);
				}
			}
		}
	}

	private void desactivatePush() {
		Consumer<Cell> removeElement = s -> {
			s.isPushable();
		};
		for (int i = 0; i < g.grid.size(); i++) {
			g.grid.get(i).stream().filter(r -> r.isMaterial()).forEach(removeElement);
		}
	}

	private void pushRule(ArrayList<Cell> rule) {
		desactivatePush();
		Consumer<Cell> newPush = s -> {
			s.setPushable(true);
		};
		if (rule.stream().anyMatch(r -> r.property().equals("push"))) { // array containing you as action
			Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
			if (c != null) {
				for (int i = 0; i < g.grid.size(); i++) {
					g.grid.get(i).stream().filter(r -> r.isMaterial()).filter(r -> r.property().equals(c.property()))
							.forEach(newPush);
				}
			}
		}
	}
	
	
	private void desactivateStop() {
		Consumer<Cell> removeElement = s -> {
			s.setStop(false);
		};
		for (int i = 0; i < g.grid.size(); i++) {
			g.grid.get(i).stream().filter(r -> r.isMaterial()).forEach(removeElement);
		}
	}
	
	private void stopRule(ArrayList<Cell> rule) {
		desactivateStop();
		Consumer<Cell> newStop = s -> {
			s.setStop(true);
		};
		if (rule.stream().anyMatch(r -> r.property().equals("stop"))) { // array containing you as action
			Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
			if (c != null) {
				for (int i = 0; i < g.grid.size(); i++) {
					g.grid.get(i).stream().filter(r -> r.isMaterial()).filter(r -> r.property().equals(c.property()))
							.forEach(newStop);
				}
			}
		}
	}
	

	private Cell lookup(Cell c, Direction d, String identity) {
		if (c != null && g.possibleToMove(d, c.getPositionX(), c.getPositionY()) && c.identity().equals(identity)) {
			return g.grid.get(c.getPositionX() + d.x).get(c.getPositionY() + d.y);
		}
		return null;
	}

	private ArrayList<Cell> verification(Cell c, Direction d) {
		ArrayList<Cell> result = new ArrayList<Cell>();
		// System.out.println("commence " + c.toString() + " dir " + d.toString());
		Cell c1 = lookup(c, d, "word");
		Cell c2 = lookup(c1, d, "operator");
		if (c1 != null && c2 != null && lookup(c2, d, "action") != null) {
			Collections.addAll(result, c, c1, c2);
			return result;
		}
		return null;
	}
}
