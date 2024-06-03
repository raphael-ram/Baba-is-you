/**
 * 
 */
package projet.Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public class Behaviour {

	private Grid g;
	private Rule rule;
	private ArrayList<LinkedList<Cell>> pawns;
	private String play = "continue";

	public Behaviour(Grid g) {
		this.g = g;
		this.rule = new Rule(g);
	}

	public String play() {
		return this.play;
	}

	public void processing() {
		this.rule.deactivateAllRules();
		this.rule.rulesApplication();
	}

	private ArrayList<LinkedList<Cell>> retrieveCurrentPawn() {
		ArrayList<LinkedList<Cell>> result = new ArrayList<>();
		g.grid.stream().flatMap(List::stream).forEach(t -> {
			if (t.getFirst().isPawn() && t.getFirst().isMaterial()) {
				result.add(t);
			}
		});

		return result;
	}

	/**
	 * Simple movement : to deplace the pawn immediately next
	 * 
	 * @param moveX nextPositionX
	 * @param moveY nextPositionY
	 * @param l     current Pawn cell
	 * @param d     direction to move
	 * @return true if movement have been made and false if not
	 */
	private boolean move(int moveX, int moveY, LinkedList<Cell> l, Direction d) {
		if (g.possibleToMove(d, moveX, moveY)) {
//			System.err.println("Principal x " + l.getFirst().getPositionX() + " y " + l.getFirst().getPositionY());
//			System.err.println("Move x " + moveX + " y " + moveY);
			if (g.grid.get(moveX).get(moveY).getFirst().isElement()) {
				g.grid.get(moveX).get(moveY).addFirst(l.getFirst());
				g.grid.get(moveX).get(moveY).getFirst().update_position(moveX, moveY);
				l.removeFirst();
			}

			return true;
		}
		return false;
	}

	/**
	 * Push movement : if the element in front is pushable
	 * 
	 * @param moveX nextPositionX
	 * @param moveY nextPositionY
	 * @param l     current Pawn cell
	 * @param d     direction to move
	 * @return true if movement have been made and false if not
	 */

	public boolean push(int moveX, int moveY, LinkedList<Cell> l, Direction d) {
		List<String> ids = List.of("word", "operator", "action");
		if (g.possibleToMove(d, moveX, moveY)) {
			if (g.grid.get(moveX).get(moveY).getFirst().isPushable()) {
				push(moveX + d.x, moveY + d.y, g.grid.get(moveX).get(moveY), d);
				if (ids.contains(g.grid.get(moveX).get(moveY).getFirst().identity())) {	
						this.rule.deactivateAllRules();
						this.rule.rulesApplication();
				}
			}
			move(moveX, moveY, l, d);
			return true;
		}
		return false;
	}

	/**
	 * Move to a Win case : if the element next is win
	 * 
	 * @param moveX nextPositionX
	 * @param moveY nextPositionY
	 * @param l     current Pawn cell
	 * @param d     direction to move
	 * @return true if movement have been made and false if not (level is win or
	 *         not)
	 */
	public boolean win(int moveX, int moveY, LinkedList<Cell> l, Direction d) {
		if (g.possibleToMove(d, moveX, moveY)) {
			if (g.grid.get(moveX).get(moveY).getFirst().isWin()) {
				g.grid.get(moveX).get(moveY).addFirst(l.getFirst());
				g.grid.get(moveX).get(moveY).getFirst().update_position(moveX, moveY);
				l.removeFirst();
				return true;
			}
		}
		return false;
	}

	/**
	 * Move to one of the defeated cases : if the element next is over
	 * 
	 * @param moveX nextPositionX
	 * @param moveY nextPositionY
	 * @param l     current Pawn cell
	 * @param d     direction to move
	 * @return true if movement have been made and false if not (level is win or
	 *         not)
	 */
	public boolean over(int moveX, int moveY, LinkedList<Cell> l, Direction d) {
		if (g.possibleToMove(d, moveX, moveY)) {
			if (g.grid.get(moveX).get(moveY).getFirst().isOver()) {
				l.removeFirst();
				return true;
			}
		}
		return false;
	}

	// public boolean

//	public boolean reverse(int moveX, int moveY, LinkedList<Cell> l, Direction d) {
//		return switch(d) {
//		case Down -> move(moveX, moveY, l, Direction.Up);
//		case Up -> move(moveX, moveY, l, Direction.Down);
//		case Left -> move(moveX, moveY, l, Direction.Right);
//		case Right -> move(moveX, moveY, l, Direction.Left);
//		default ->
//			throw new IllegalArgumentException("Unexpected value: " + d); 
//		};
//			
//		
//	}

	public void movementManager(Direction d) {
		pawns = retrieveCurrentPawn();
		pawns.stream().forEach(s -> {
			var moveX = s.getFirst().getPositionX() + d.x;
			var moveY = s.getFirst().getPositionY() + d.y;

			System.err.println("OUIIIII" + s + " vers " + g.grid.get(moveX).get(moveY) + " isWho"
					+ g.grid.get(moveX).get(moveY).getFirst().isPushable());

			if (over(moveX, moveY, s, d))
				pawns = retrieveCurrentPawn();
			if (move(moveX, moveY, s, d))
				pawns = retrieveCurrentPawn();
			if (push(moveX, moveY, s, d))
				pawns = retrieveCurrentPawn();
			if (win(moveX, moveY, s, d)) {
				pawns = retrieveCurrentPawn();
				this.play = "win";
				System.err.println("They made it" + this.play);
			}

		});
		if (pawns.size() == 0) {
			this.play = "loose";
			System.out.println("Vide " + pawns);
		} else {

			System.out.println("RRR" + pawns.get(0).getFirst() + " x " + pawns.get(0).getFirst().getPositionX() + " y "
					+ pawns.get(0).getFirst().getPositionY());
		}

	}

}
