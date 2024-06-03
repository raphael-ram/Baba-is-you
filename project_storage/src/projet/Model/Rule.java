/**
 * 
 */
package projet.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Rule {
	private Grid g;

	public Rule(Grid g) {
		this.g = g;
	}

	/**
	 * Research available rules in the games and configure the cells according to
	 * them
	 */
	private ArrayList<ArrayList<Cell>> researchRule() {
		ArrayList<ArrayList<Cell>> rules = new ArrayList<>();
		List<Direction> directions = List.of(Direction.Right, Direction.Down);
		Consumer<Cell> applyRuleOnCells = s -> {
			for (Direction d : directions) {
				if (verification(s, d) != null) {
					System.out.println("WOLOLOLOO" + verification(s, d).toString());
					rules.add(verification(s, d));
				}
			}
		};
		g.grid.stream().flatMap(List::stream).flatMap(List::stream).filter(r -> r.isProperty()).forEach(applyRuleOnCells); // on property rules
		System.out.println("garou " + rules.toString());
		return rules;
	}

	/**
	 * Verify if it is a rule
	 * 
	 * @param c cell potientially a rule
	 * @param d valid direction of reading a rule
	 */
	private ArrayList<Cell> verification(Cell c, Direction d) {
		ArrayList<Cell> result = new ArrayList<Cell>();
		Cell c1 = lookup(c, d, "word");
		Cell c2 = lookup(c1, d, "operator");
		if (c1 != null && c2 != null && lookup(c2, d, "action") != null) {
			Collections.addAll(result, c, c1, c2);
			return result;
		}
		return null;
	}

	/**
	 * Check if the next elements of the cell compose the rule
	 * 
	 * @param c        cell potientially a rule
	 * @param d        valid direction of reading a rule
	 * @param identity which type of rule cell it is (word, operator or action)
	 */
	private Cell lookup(Cell c, Direction d, String identity) {
		if (c != null && g.possibleToMove(d, c.getPositionX(), c.getPositionY()) && c.identity().equals(identity)) {
			return g.grid.get(c.getPositionX() + d.x).get(c.getPositionY() + d.y).getFirst();
		}
		return null;
	}
	
	
	

	/**
	 * Apply all the existing rules to the concerned cells
	 */
	public void rulesApplication() {
		ArrayList<ArrayList<Cell>> rules = researchRule();
		Consumer<ArrayList<Cell>> applyRule = s -> {
			pushRule(s);
			youRule(s);
			winRule(s);
			overRule(s);
			stopRule(s);
			reverseRule(s);
		};
		rules.stream().forEach(applyRule);
	}
	
	
	/**
	 * Deactivate all the existing rules 
	 */
	public void deactivateAllRules() {
		deactivateOver();
		deactivatePush();
		deactivateReverse();
		deactivateStop();
		deactivateWin();
		deactivateYou();
	}
	
	

	/**
	 * Deactivate Push action on the cells which it is applied on
	 */
	private void deactivatePush() {
		Consumer<LinkedList<Cell>> deactivatElement = s -> {
			if (s.getFirst().isPushable() && s.getFirst().isMaterial()) {
				Cell first = s.getFirst();
				s.removeLast();
				s.addFirst(new Element(String.valueOf(first.property().charAt(0)).toUpperCase(), first.getPositionX(),
						first.getPositionY()));
				System.err.println("here is " + s);
			}

		};
		g.grid.stream().flatMap(List::stream).forEach(deactivatElement);
	}

	/**
	 * Apply Push action on the cell
	 * 
	 * @param rule contains a word, an operator and an action
	 */
	private void pushRule(ArrayList<Cell> rule) {
		if (rule.stream().anyMatch(r -> r.property().equals("push"))) {// array containing push as action
			Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
			if (c != null) {
				Consumer<LinkedList<Cell>> makeElementPushable = p -> {
					if ((p.getFirst().isElement() && p.getFirst().property() != "*"
							&& p.getLast().property().equals(c.property()))) {
						Cell tmp = p.getLast();
						tmp.setPushable(true);
						p.clear();
						p.addFirst(tmp);
						p.addLast(new Element("*", tmp.getPositionX(), tmp.getPositionY()));
					} else if (p.getFirst().isMaterial() && p.getFirst().property().equals(c.property())) {
						p.getFirst().setPushable(true);
					}
				};
				g.grid.stream().flatMap(List::stream).forEach(makeElementPushable);
			}
		}
	}
	
	
	

	/**
	 * Deactivate Stop action on the cells which it is applied on
	 */
	private void deactivateStop() {
		Consumer<LinkedList<Cell>> deactivatElement = s -> {
			if (s.getFirst().isStop() && s.getFirst().isMaterial()) {
				Cell first = s.getFirst();
				s.removeLast();
				s.addFirst(new Element(String.valueOf(first.property().charAt(0)).toUpperCase(), first.getPositionX(),
						first.getPositionY()));
				System.err.println("here is " + s);
			}
		};
		g.grid.stream().flatMap(List::stream).forEach(deactivatElement);
	}
	
	/**
	 * Apply Stop action on the cell
	 * 
	 * @param rule contains a word, an operator and an action
	 */
	private void stopRule(ArrayList<Cell> rule) {
		if (rule.stream().anyMatch(r -> r.property().equals("stop"))) {// array containing stop as action
			Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
			if (c != null) {
				Consumer<LinkedList<Cell>> makeElementStop= p -> {
					if ((p.getFirst().isElement() && p.getFirst().property() != "*"
							&& p.getLast().property().equals(c.property()))) {
						Cell tmp = p.getLast();
						tmp.setStop(true);
						p.clear();
						p.addFirst(tmp);
						p.addLast(new Element("*", tmp.getPositionX(), tmp.getPositionY()));
					} else if (p.getFirst().isMaterial() && p.getFirst().property().equals(c.property())) {
						p.getFirst().setStop(true);
					}
				};
				g.grid.stream().flatMap(List::stream).forEach(makeElementStop);
			}
		}
	}
	
	
	
	/**
	 * Deactivate Win action on the cells which it is applied on
	 */
	private void deactivateWin() {
		Consumer<LinkedList<Cell>> deactivatElement = s -> {
			if (s.getFirst().isWin() && s.getFirst().isMaterial()) {
				Cell first = s.getFirst();
				s.removeLast();
				s.addFirst(new Element(String.valueOf(first.property().charAt(0)).toUpperCase(), first.getPositionX(),
						first.getPositionY()));
				System.err.println("here is " + s);
			}
		};
		g.grid.stream().flatMap(List::stream).forEach(deactivatElement);
	}
	
	/**
	 * Apply Win action on the cell
	 * 
	 * @param rule contains a word, an operator and an action
	 */
	private void winRule(ArrayList<Cell> rule) {
		if (rule.stream().anyMatch(r -> r.property().equals("win"))) {// array containing win as action
			Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
			if (c != null) {
				Consumer<LinkedList<Cell>> makeElementStop= p -> {
					if ((p.getFirst().isElement() && p.getFirst().property() != "*"
							&& p.getLast().property().equals(c.property()))) {
						Cell tmp = p.getLast();
						tmp.setWin(true);
						p.clear();
						p.addFirst(tmp);
						p.addLast(new Element("*", tmp.getPositionX(), tmp.getPositionY()));
					} else if (p.getFirst().isMaterial() && p.getFirst().property().equals(c.property())) {
						p.getFirst().setWin(true);
					}
				};
				g.grid.stream().flatMap(List::stream).forEach(makeElementStop);
			}
		}
	}
	
	
	
	
	/**
	 * Deactivate You action on the cells which it is applied on
	 */
	private void deactivateYou() {
		Consumer<LinkedList<Cell>> deactivatElement = s -> {
			if (s.getFirst().isPawn() && s.getFirst().isMaterial()) {
				Cell first = s.getFirst();
				s.removeLast();
				s.addFirst(new Element(String.valueOf(first.property().charAt(0)).toUpperCase(), first.getPositionX(),
						first.getPositionY()));
				System.err.println("here is " + s);
			}
		};
		g.grid.stream().flatMap(List::stream).forEach(deactivatElement);
	}
	
	
	/**
	 * Apply You action on the cell
	 * 
	 * @param rule contains a word, an operator and an action
	 */
	private void youRule(ArrayList<Cell> rule) {
		if (rule.stream().anyMatch(r -> r.property().equals("you"))) {// array containing you as action
			Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
			if (c != null) {
				Consumer<LinkedList<Cell>> makeElementStop= p -> {
					if ((p.getFirst().isElement() && p.getFirst().property() != "*"
							&& p.getLast().property().equals(c.property()))) {
						Cell tmp = p.getLast();
						tmp.setBaba(true);
						p.clear();
						p.addFirst(tmp);
						p.addLast(new Element("*", tmp.getPositionX(), tmp.getPositionY()));
					} else if (p.getFirst().isMaterial() && p.getFirst().property().equals(c.property())) {
						p.getFirst().setBaba(true);
					}
				};
				g.grid.stream().flatMap(List::stream).forEach(makeElementStop);
			}
		}
	}
	
	
	/**
	 * Deactivate You action on the cells which it is applied on
	 */
	private void deactivateReverse() {
		Consumer<LinkedList<Cell>> deactivatElement = s -> {
			if (s.getFirst().isOver() && s.getFirst().isMaterial()) {
				Cell first = s.getFirst();
				s.removeLast();
				s.addFirst(new Element(String.valueOf(first.property().charAt(0)).toUpperCase(), first.getPositionX(),
						first.getPositionY()));
				System.err.println("here is " + s);
			}
		};
		g.grid.stream().flatMap(List::stream).forEach(deactivatElement);
	}
	
	
	/**
	 * Apply Reverse action on the cell
	 * 
	 * @param rule contains a word, an operator and an action
	 */
	private void reverseRule(ArrayList<Cell> rule) {
		if (rule.stream().anyMatch(r -> r.property().equals("reverse"))) {// array containing you as action
			Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
			if (c != null) {
				Consumer<LinkedList<Cell>> makeElementStop= p -> {
					if ((p.getFirst().isElement() && p.getFirst().property() != "*"
							&& p.getLast().property().equals(c.property()))) {
						Cell tmp = p.getLast();
						tmp.setReverse(true);
						p.clear();
						p.addFirst(tmp);
						p.addLast(new Element("*", tmp.getPositionX(), tmp.getPositionY()));
					} else if (p.getFirst().isMaterial() && p.getFirst().property().equals(c.property())) {
						p.getFirst().setReverse(true);
					}
				};
				g.grid.stream().flatMap(List::stream).forEach(makeElementStop);
			}
		}
	}


	/**
	 * Deactivate You action on the cells which it is applied on
	 */
	private void deactivateOver() {
		Consumer<LinkedList<Cell>> deactivatElement = s -> {
			if (s.getFirst().isOver() && s.getFirst().isMaterial()) {
				Cell first = s.getFirst();
				s.removeLast();
				s.addFirst(new Element(String.valueOf(first.property().charAt(0)).toUpperCase(), first.getPositionX(),
						first.getPositionY()));
				System.err.println("here is " + s);
			}
		};
		g.grid.stream().flatMap(List::stream).forEach(deactivatElement);
	}
	
	
	/**
	 * Apply Over action on the cell
	 * 
	 * @param rule contains a word, an operator and an action
	 */
	private void overRule(ArrayList<Cell> rule) {
		if (rule.stream().anyMatch(r -> r.property().equals("over"))) {// array containing you as action
			Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
			if (c != null) {
				Consumer<LinkedList<Cell>> makeElementStop= p -> {
					if ((p.getFirst().isElement() && p.getFirst().property() != "*"
							&& p.getLast().property().equals(c.property()))) {
						Cell tmp = p.getLast();
						tmp.setOver(true);
						p.clear();
						p.addFirst(tmp);
						p.addLast(new Element("*", tmp.getPositionX(), tmp.getPositionY()));
					} else if (p.getFirst().isMaterial() && p.getFirst().property().equals(c.property())) {
						p.getFirst().setOver(true);
					}
				};
				g.grid.stream().flatMap(List::stream).forEach(makeElementStop);
			}
		}
	}

	
	
}
