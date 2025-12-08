package projet.Model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    private boolean applyMove(Cell mover, int targetX, int targetY, LinkedList<Cell> moverList) {
        Cell target = g.grid.get(targetX).get(targetY).getFirst();

        // Interactions
        if (target.isSink()) {
             moverList.removeFirst();
             g.grid.get(targetX).get(targetY).removeFirst();
             if (mover.isPawn()) this.play = "loose";
             return true;
        }
        if (mover.isMelt() && target.isHot()) {
             moverList.removeFirst();
             if (mover.isPawn()) this.play = "loose";
             return true;
        }
        if (target.isDefeat() && mover.isPawn()) {
             moverList.removeFirst();
             this.play = "loose";
             return true;
        }
        if (target.isWin() && mover.isPawn()) {
             g.grid.get(targetX).get(targetY).addFirst(mover);
             mover.update_position(targetX, targetY);
             moverList.removeFirst();
             this.play = "win";
             return true;
        }
        
        // Blocking
        if (target.isStop()) return false;
        
        // Move
        g.grid.get(targetX).get(targetY).addFirst(mover);
        mover.update_position(targetX, targetY);
        moverList.removeFirst();
        return true;
    }

    private boolean move(int moveX, int moveY, LinkedList<Cell> l, Direction d) {
        if (g.possibleToMove(d, moveX, moveY)) {
            int oldX = l.getFirst().getPositionX();
            int oldY = l.getFirst().getPositionY();
            
            if (applyMove(l.getFirst(), moveX, moveY, l)) {
                pull(oldX, oldY, d);
                return true;
            }
        }
        return false;
    }

    private void pull(int targetX, int targetY, Direction d) {
        int sourceX = targetX - d.x;
        int sourceY = targetY - d.y;
        if (g.possibleToMove(d, sourceX, sourceY)) {
            LinkedList<Cell> sourceList = g.grid.get(sourceX).get(sourceY);
            if (!sourceList.isEmpty() && sourceList.getFirst().isPull() && sourceList.getFirst().isMaterial()) {
                 Cell c = sourceList.getFirst();
                 if (applyMove(c, targetX, targetY, sourceList)) {
                     pull(sourceX, sourceY, d);
                 }
            }
        }
    }

    public boolean push(int moveX, int moveY, LinkedList<Cell> l, Direction d) {
        if (g.possibleToMove(d, moveX, moveY)) {
            if (g.grid.get(moveX).get(moveY).getFirst().isPushable()) {
                if (push(moveX + d.x, moveY + d.y, g.grid.get(moveX).get(moveY), d)) {
                    move(moveX, moveY, l, d);
                }
                return true;
            }
            if (!g.grid.get(moveX).get(moveY).getFirst().isStop()) {
                move(moveX, moveY, l, d);
                return true;
            }
        }
        return false;
    }

    public void movementManager(Direction d) {
        pawns = retrieveCurrentPawn();
        List<LinkedList<Cell>> currentPawns = new ArrayList<>(pawns);
        for (LinkedList<Cell> s : currentPawns) {
            var moveX = s.getFirst().getPositionX() + d.x;
            var moveY = s.getFirst().getPositionY() + d.y;

            if (push(moveX, moveY, s, d)) {
                continue;
            }
            if (move(moveX, moveY, s, d)) {
                continue;
            }
        }

        if (pawns.size() == 0) {
            this.play = "loose";
        }

        // Appliquer et afficher les règles après chaque déplacement
        processing();
    }

    // Méthode pour afficher la grille avec la liste des éléments dans chaque cellule
    public void displayGrid() {
        for (int i = 0; i < g.getNbLines(); i++) {
            for (int j = 0; j < g.getNbColumns(); j++) {
                LinkedList<Cell> cellList = g.grid.get(i).get(j);
                if (!cellList.isEmpty()) {
                    // Afficher tous les éléments dans la cellule
                    System.out.print("[");
                    for (Cell cell : cellList) {
                        System.out.print(cell.property() + ", ");
                    }
                    System.out.print("] ");
                } else {
                    System.out.print("[ ] "); // Cellule vide
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // Méthode pour afficher les règles actives
    public void displayActiveRules() {
        List<String> activeRules = rule.getActiveRules();
        System.out.println("Règles actives :");
        for (String rule : activeRules) {
            System.out.println(rule);
        }
        System.out.println();
    }
}
