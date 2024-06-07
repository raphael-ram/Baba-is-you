package projet.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class Rule {
    private Grid g;
    private Transmutation transmutation;

    public Rule(Grid g) {
        this.g = g;
        this.transmutation = new Transmutation(g);
    }

    /**
     * Research available rules in the games and configure the cells according to
     * them
     */
    public ArrayList<ArrayList<Cell>> researchRule() {
        ArrayList<ArrayList<Cell>> rules = new ArrayList<>();
        List<Direction> directions = List.of(Direction.Right, Direction.Down);
        Consumer<Cell> applyRuleOnCells = s -> {
            for (Direction d : directions) {
                if (verification(s, d) != null) {
                    rules.add(verification(s, d));
                }
            }
        };
        g.grid.stream().flatMap(List::stream).flatMap(List::stream).filter(r -> r.isProperty()).forEach(applyRuleOnCells);
        return rules;
    }

    /**
     * Verify if it is a rule
     *
     * @param c cell potentially a rule
     * @param d valid direction of reading a rule
     */
    private ArrayList<Cell> verification(Cell c, Direction d) {
        ArrayList<Cell> result = new ArrayList<>();
        Cell c1 = lookup(c, d, "word");
        Cell c2 = lookup(c1, d, "operator");
        if (c1 != null && c2 != null && lookup(c2, d, "action") != null) {
            Collections.addAll(result, c, c1, c2);
            return result;
        }
        if (c1 != null && c2 != null && lookup(c2, d, "word") != null) {
            Collections.addAll(result, c, c1, c2, lookup(c2, d, "word"));
            return result;
        }
        return null;
    }

    /**
     * Check if the next elements of the cell compose the rule
     *
     * @param c        cell potentially a rule
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
            transformRule(s); // Apply transformation rules
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
                first.setPushable(false);
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
        if (rule.stream().anyMatch(r -> r.property().equals("push"))) {
            Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
            if (c != null) {
                Consumer<LinkedList<Cell>> makeElementPushable = p -> {
                    if (p.getFirst().property().equals(c.property())) {
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
                first.setStop(false);
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
        if (rule.stream().anyMatch(r -> r.property().equals("stop"))) {
            Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
            if (c != null) {
                Consumer<LinkedList<Cell>> makeElementStop = p -> {
                    if (p.getFirst().property().equals(c.property())) {
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
                first.setWin(false);
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
        if (rule.stream().anyMatch(r -> r.property().equals("win"))) {
            Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
            if (c != null) {
                Consumer<LinkedList<Cell>> makeElementWin = p -> {
                    if (p.getFirst().property().equals(c.property())) {
                        p.getFirst().setWin(true);
                    }
                };
                g.grid.stream().flatMap(List::stream).forEach(makeElementWin);
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
                first.setPawn(false);
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
        if (rule.stream().anyMatch(r -> r.property().equals("you"))) {
            Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
            if (c != null) {
                Consumer<LinkedList<Cell>> makeElementYou = p -> {
                    if (p.getFirst().property().equals(c.property())) {
                        p.getFirst().setPawn(true);
                    }
                };
                g.grid.stream().flatMap(List::stream).forEach(makeElementYou);
            }
        }
    }

    /**
     * Deactivate Reverse action on the cells which it is applied on
     */
    private void deactivateReverse() {
        Consumer<LinkedList<Cell>> deactivatElement = s -> {
            if (s.getFirst().isReverse() && s.getFirst().isMaterial()) {
                Cell first = s.getFirst();
                first.setReverse(false);
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
        if (rule.stream().anyMatch(r -> r.property().equals("reverse"))) {
            Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
            if (c != null) {
                Consumer<LinkedList<Cell>> makeElementReverse = p -> {
                    if (p.getFirst().property().equals(c.property())) {
                        p.getFirst().setReverse(true);
                    }
                };
                g.grid.stream().flatMap(List::stream).forEach(makeElementReverse);
            }
        }
    }

    /**
     * Deactivate Over action on the cells which it is applied on
     */
    private void deactivateOver() {
        Consumer<LinkedList<Cell>> deactivatElement = s -> {
            if (s.getFirst().isOver() && s.getFirst().isMaterial()) {
                Cell first = s.getFirst();
                first.setOver(false);
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
        List<String> overwords = List.of("melt", "sink", "defeat");
        if (rule.stream().anyMatch(r -> overwords.contains(r.property()))) {
            Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
            if (c != null) {
                Consumer<LinkedList<Cell>> makeElementOver = p -> {
                    if (p.getFirst().property().equals(c.property())) {
                        p.getFirst().setOver(true);
                    }
                };
                g.grid.stream().flatMap(List::stream).forEach(makeElementOver);
            }
        }
    }

    /**
     * Apply Transform action on the cell
     * 
     * @param rule contains a noun, an operator, and another noun
     */
    private void transformRule(ArrayList<Cell> rule) {
        if (rule.size() == 4) {
            Cell noun1 = rule.get(0);
            Cell noun2 = rule.get(2);
            if (noun1.identity().equals("word") && noun2.identity().equals("word")) {
                transmutation.transform(noun1.property(), noun2.property());
            }
        }
    }

    /**
     * Return all active rules as a list of strings for display purposes
     */
    public List<String> getActiveRules() {
        ArrayList<ArrayList<Cell>> rules = researchRule();
        List<String> activeRules = new ArrayList<>();
        for (ArrayList<Cell> rule : rules) {
            StringBuilder ruleStr = new StringBuilder();
            for (Cell cell : rule) {
                ruleStr.append(cell.property()).append(" ");
            }
            activeRules.add(ruleStr.toString().trim());
        }
        return activeRules;
    }
}
