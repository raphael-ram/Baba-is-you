/**
 * 
 */
package projet.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Rule {
	private final Grid g;

	public Rule(Grid g) {
		this.g = g;
	}

	/**
	 * Research available rules in the games and configure the cells according to
	 * them
	 */
	public void researchRule() {

		List<Direction> directions = List.of(Direction.Right, Direction.Down);
		Consumer<Cell> applyRuleOnCells = s -> {
			for (Direction d : directions) {
				if (verification(s, d) != null) {
					rulesApplication(s, d);
				}
			}
		};
		Grid.grid.stream().flatMap(List::stream).filter(r -> r.isProperty()).forEach(applyRuleOnCells); // on property only because we are looking for the element of a rule and according to them we apply what should be applied

	}

	/**
	 * Apply rules on the cells if possible
	 * 
	 * @param c cell
	 * @param d direction
	 */
	private void rulesApplication(Cell c, Direction d) {
		youRule(verification(c, d));
		pushRule(verification(c, d));
		winRule(verification(c, d));
		overRule(verification(c, d));
		stopRule(verification(c, d));
	}

	/**
	 *  Deactivate You action on the cells which it is applied on
	 */
	private void deactivateYou() {
		Consumer<Cell> removeElement = s -> {
			s.setBaba(false);
		};
			Grid.grid.stream().flatMap(List::stream).forEach(removeElement);
	}

	/**
	 * Apply You action on the cell
	 * 
	 * @param rule the entire rule
	 */
	private void youRule(ArrayList<Cell> rule) {
		if (rule.stream().anyMatch(r -> r.property().equals("you"))) {// array containing you as action
			deactivateYou();
			Consumer<Cell> newPown = s -> {
				s.setBaba(true);
			};
			Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null); // get word of this
																																																// array
			if (c != null) {
				Grid.grid.stream().flatMap(List::stream).filter(r -> r.isMaterial()).filter(r -> r.property().equals(c.property()))
						.forEach(newPown);
			}
		}
	}

	/**
	 * Deactivate Push action on the cells which it is applied on
	 */
	private void deactivatePush() {
		Consumer<Cell> removeElement = s -> {
			s.setPushable(false);
		};
		Grid.grid.stream().flatMap(List::stream).filter(r -> r.isMaterial()).forEach(removeElement);
	}

	/**
	 * Apply Push action on the cell
	 * 
	 * @param rule the entire rule
	 */
	private void pushRule(ArrayList<Cell> rule) {
		
		if (rule.stream().anyMatch(r -> r.property().equals("push"))) {// array containing push as action
			//deactivatePush();
			deactivatePush();
			Consumer<Cell> newPush = s -> {
				s.setPushable(true);
				System.out.println("push s is " + s.toString());

			};

			Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
			if (c != null) {
				Grid.grid.stream().flatMap(List::stream).filter(r -> r.isMaterial()).filter(r -> r.property().equals(c.property())).forEach(newPush);
			}
		}
	}

	/**
	 * Deactivate Stop action on the cells which it is applied on
	 */
	private void deactivateStop() {
		Consumer<Cell> removeElement = s -> {
			s.setStop(false);
		};
		Grid.grid.stream().flatMap(List::stream).filter(r -> r.isMaterial()).forEach(removeElement);
	}

	/**
	 * Apply Stop action on the cell
	 * 
	 * @param rule the entire rule
	 */
	private void stopRule(ArrayList<Cell> rule) {
		deactivateStop();

		if (rule.stream().anyMatch(r -> r.property().equals("stop"))) { // array containing stop as action
			System.out.println("ENther this");
			Consumer<Cell> newStop = s -> {
				s.setStop(true);
				System.out.println("stop s is " + s.toString() + " gg " + s.isStop());
			};
			Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
			if (c != null) {
				Grid.grid.stream().flatMap(List::stream).filter(r -> r.isMaterial()).filter(r -> r.property().equals(c.property()))
						.forEach(newStop);
			}
		}
	}

	/**
	 * Deactivate Win action on the cells which it is applied on
	 */
	private void deactivateWin() {
		Consumer<Cell> removeElement = s -> {
			s.setWin(false);
		};
		Grid.grid.stream().flatMap(List::stream).filter(r -> r.isMaterial()).forEach(removeElement);

	}

	/**
	 * Apply Win action on the cell
	 * 
	 * @param rule the entire rule
	 */
	private void winRule(ArrayList<Cell> rule) {
		if (rule.stream().anyMatch(r -> r.property().equals("win"))) { // array containing win as action
			deactivateWin();
			Consumer<Cell> newWin = s -> {
				s.setWin(true);
				System.out.println("win s is " + s.toString());

			};
			Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
			if (c != null) {

				Grid.grid.stream().flatMap(List::stream).filter(r -> r.isMaterial()).filter(r -> r.property().equals(c.property()))
						.forEach(newWin);

			}
		}
	}

	/**
	 * Deactivate Over action on the cells which it is applied on
	 */
	private void deactivateOver() {
		Consumer<Cell> removeElement = s -> {
			s.setOver(false);
		};
		Grid.grid.stream().flatMap(List::stream).filter(r -> r.isMaterial()).forEach(removeElement);
	}

	/**
	 * Apply Over(defeat, melt, sink) action on the cell
	 * 
	 * @param rule the entire rule
	 */
	private void overRule(ArrayList<Cell> rule) {
		if (rule.stream().anyMatch(r -> List.of("sink", "melt", "defeat").contains(r.property()))) { // array containing
																																																	// over as action
			deactivateOver();
			Consumer<Cell> newOver = s -> {
				s.setOver(true);
			};
			Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
			if (c != null) {
				Grid.grid.stream().flatMap(List::stream).filter(r -> r.isMaterial()).filter(r -> r.property().equals(c.property())).forEach(newOver);
			}
		}
	}

	/**
	 * Check if the next elements of the cell compose the rule
	 * 
	 * @param c        cell potientially a rule
	 * @param d        valid direction of reading a rule
	 * @param identity which type of rule cell it is (word, operator or action)
	 */
	private Cell lookup(Cell c, Direction d, String identity) {
		if (c != null && Grid.possibleToMove(d, c.getPositionX(), c.getPositionY()) && c.identity().equals(identity)) {
			return Grid.grid.get(c.getPositionX() + d.x).get(c.getPositionY() + d.y);
		}
		return null;
	}

	/**
	 * Verify if it is a rule
	 * 
	 * @param c cell potientially a rule
	 * @param d valid direction of reading a rule
	 */
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
