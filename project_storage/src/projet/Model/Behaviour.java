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
	private boolean play;

	public Behaviour(Grid g) {
		this.g = g;
		this.rule = new Rule(g);
	}

	public void process() {
		System.out.println(g.toString());

		this.rule.rulesApplication();
//		//test PUSH RULE
//		movementManager(Direction.Left);
//		System.out.println(g.toString());
////		movement(Direction.Up);
////		System.out.println(g.toString());
//		var ff = 9;
//		while(ff > 0) {
//			movementManager(Direction.Up);
//			ff--;
//		}
//		System.out.println(g.toString());
//		
		movementManager(Direction.Down);
	System.out.println(g.toString());
		
//	System.out.println(g.toString());
//		movementManager(Direction.Down);
//		System.out.println(g.toString());
//		var df = 7;
//		while(df > 0) {
//			movementManager(Direction.Up);
//			df--;
//		}
//		System.out.println(g.toString());
//		while(df < 7) {
//			movementManager(Direction.Down);
//			df++;
//		}
//		while(ff < 3) {
//			movementManager(Direction.Right);
//			ff++;
//		}
//		System.out.println(g.toString());
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
	 * @param moveX nextPositionX
	 * @param moveY nextPositionY
	 * @param l current Pawn cell
	 * @param d direction to move
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
	 * @param moveX nextPositionX
	 * @param moveY nextPositionY
	 * @param l current Pawn cell
	 * @param d direction to move
	 * @return true if movement have been made and false if not
	 */
	
	public boolean push(int moveX, int moveY, LinkedList<Cell> l, Direction d) {
		if (g.possibleToMove(d, moveX, moveY)) {
			if (g.grid.get(moveX).get(moveY).getFirst().isPushable()) {
				push(moveX + d.x, moveY + d.y, g.grid.get(moveX).get(moveY), d);
				if(!g.grid.get(moveX).get(moveY).getFirst().isMaterial()) {
					move(moveX, moveY, l, d);// COMPORTEMENT A ANALYSER PLUS TARD
					this.rule.deactivateAllRules();
					this.rule.rulesApplication();
					move(moveX, moveY, l, d);
					return true;
				}
			}
			move(moveX, moveY, l, d);
			return true;
		}
		return false;
	}

	
	/**
	 * Move to a Win case : if the element next is win
	 * @param moveX nextPositionX
	 * @param moveY nextPositionY
	 * @param l current Pawn cell
	 * @param d direction to move
	 * @return true if movement have been made and false if not (level is win or not)
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
	 * @param moveX nextPositionX
	 * @param moveY nextPositionY
	 * @param l current Pawn cell
	 * @param d direction to move
	 * @return true if movement have been made and false if not (level is win or not)
	 */
	public boolean over(int moveX, int moveY, LinkedList<Cell> l, Direction d) {
		if(g.possibleToMove(d, moveX, moveY)) {
			if(g.grid.get(moveX).get(moveY).getFirst().isOver()){
				l.removeFirst();
				return true;
			}
		}
		return false;
	}
	
	
	//public boolean 
	
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
		play = false;
		pawns = retrieveCurrentPawn();
		pawns.stream().forEach(s -> {
			var moveX = s.getFirst().getPositionX() + d.x;
			var moveY = s.getFirst().getPositionY() + d.y;

			System.err.println("OUIIIII" + s + " vers " + g.grid.get(moveX).get(moveY) + " isWho"
					+ g.grid.get(moveX).get(moveY).getFirst().isPushable());

			if(over(moveX, moveY, s, d)) {
				System.err.println("snxsnjxj");
				pawns = retrieveCurrentPawn();
			}
			if(move(moveX, moveY, s, d)) {
				pawns = retrieveCurrentPawn();
			}
			if(push(moveX, moveY, s, d)) {
				pawns = retrieveCurrentPawn();
			}
			
			if(win(moveX, moveY, s, d)) {
				System.err.println("They made it");
				pawns = retrieveCurrentPawn();
				play = true;
			}
			
			
			


		});
		if(pawns.size() > 0) {
			System.out.println("RRR" + pawns.get(0).getFirst() + " x " + pawns.get(0).getFirst().getPositionX() + " y "
					+ pawns.get(0).getFirst().getPositionY());
		}
		else {
			play = false;
			System.out.println("Vide " + pawns);
		}
		
		
		
		

	}

}
