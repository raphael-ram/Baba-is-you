package fr.umlv.babaisyou.model;

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
                ArrayList<ArrayList<Cell>> foundRules = verification(s, d);
                if (!foundRules.isEmpty()) {
                    rules.addAll(foundRules);
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
    private ArrayList<ArrayList<Cell>> verification(Cell c, Direction d) {
        ArrayList<ArrayList<Cell>> validRules = new ArrayList<>();

        // Start must be a word
        if (!c.identity().equals("word")) {
            return validRules;
        }

        int x = c.getPositionX();
        int y = c.getPositionY();

        // Find all Operators at x+d.x, y+d.y
        List<Cell> operators = findCellsByIdentity(x + d.x, y + d.y, "operator");
        if (operators.isEmpty()) {
            return validRules;
        }

        for (Cell c1 : operators) {
            // Find all Actions or Words at x+2d.x, y+2d.y
            List<Cell> nextCells = findCellsByIdentity(x + d.x * 2, y + d.y * 2, "action");
            nextCells.addAll(findCellsByIdentity(x + d.x * 2, y + d.y * 2, "word"));

            for (Cell c2 : nextCells) {
                ArrayList<Cell> rule = new ArrayList<>();
                Collections.addAll(rule, c, c1, c2);
                validRules.add(rule);
            }
        }
        return validRules;
    }

    /**
     * Find all cells with a specific identity at a given position
     */
    private List<Cell> findCellsByIdentity(int x, int y, String identity) {
        List<Cell> matchingCells = new ArrayList<>();
        // Check bounds: x is row (lines), y is col (columns)
        if (x < 0 || x >= g.getNbLines() || y < 0 || y >= g.getNbColumns()) {
            return matchingCells;
        }
        
        // Direct access to grid
        List<Cell> cells = g.grid.get(x).get(y);
        for (Cell cell : cells) {
            if (cell.identity().equals(identity)) {
                matchingCells.add(cell);
            }
        }
        return matchingCells;
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
            meltRule(s);
            hotRule(s);
            sinkRule(s);
            defeatRule(s);
            pullRule(s);
            stopRule(s);
            reverseRule(s);
            transformRule(s); // Apply transformation rules
        };
        rules.stream().forEach(applyRule);
    }

    /**
     * Deactivate all rules in a single grid pass instead of 10 separate passes.
     * Reduces O(10×N) to O(N) where N = number of cells.
     */
    public void deactivateAllRules() {
        g.grid.stream()
            .flatMap(List::stream)
            .map(LinkedList::getFirst)
            .filter(Cell::isMaterial)
            .forEach(cell -> {
                cell.setPushable(false);
                cell.setStop(false);
                cell.setWin(false);
                cell.setPawn(false);
                cell.setReverse(false);
                cell.setMelt(false);
                cell.setHot(false);
                cell.setSink(false);
                cell.setDefeat(false);
                cell.setPull(false);
            });
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
     * Apply Melt action
     */
    private void meltRule(ArrayList<Cell> rule) {
        if (rule.stream().anyMatch(r -> r.property().equals("melt"))) {
            Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
            if (c != null) {
                Consumer<LinkedList<Cell>> makeElementMelt = p -> {
                    if (p.getFirst().property().equals(c.property())) {
                        p.getFirst().setMelt(true);
                    }
                };
                g.grid.stream().flatMap(List::stream).forEach(makeElementMelt);
            }
        }
    }

    /**
     * Apply Hot action
     */
    private void hotRule(ArrayList<Cell> rule) {
        if (rule.stream().anyMatch(r -> r.property().equals("hot"))) {
            Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
            if (c != null) {
                Consumer<LinkedList<Cell>> makeElementHot = p -> {
                    if (p.getFirst().property().equals(c.property())) {
                        p.getFirst().setHot(true);
                    }
                };
                g.grid.stream().flatMap(List::stream).forEach(makeElementHot);
            }
        }
    }

    /**
     * Apply Sink action
     */
    private void sinkRule(ArrayList<Cell> rule) {
        if (rule.stream().anyMatch(r -> r.property().equals("sink"))) {
            Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
            if (c != null) {
                Consumer<LinkedList<Cell>> makeElementSink = p -> {
                    if (p.getFirst().property().equals(c.property())) {
                        p.getFirst().setSink(true);
                    }
                };
                g.grid.stream().flatMap(List::stream).forEach(makeElementSink);
            }
        }
    }

    /**
     * Apply Defeat action
     */
    private void defeatRule(ArrayList<Cell> rule) {
        if (rule.stream().anyMatch(r -> r.property().equals("defeat"))) {
            Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
            if (c != null) {
                Consumer<LinkedList<Cell>> makeElementDefeat = p -> {
                    if (p.getFirst().property().equals(c.property())) {
                        p.getFirst().setDefeat(true);
                    }
                };
                g.grid.stream().flatMap(List::stream).forEach(makeElementDefeat);
            }
        }
    }

    /**
     * Apply Transform action on the cell
     * 
     * @param rule contains a noun, an operator, and another noun
     */
    private void transformRule(ArrayList<Cell> rule) {
        if (rule.size() == 3) {
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
    /**
     * Apply Pull action
     */
    private void pullRule(ArrayList<Cell> rule) {
        if (rule.stream().anyMatch(r -> r.property().equals("pull"))) {
            Cell c = rule.stream().filter(r -> r.identity().equals("word")).findFirst().orElse(null);
            if (c != null) {
                Consumer<LinkedList<Cell>> makeElementPull = p -> {
                    if (p.getFirst().property().equals(c.property())) {
                        p.getFirst().setPull(true);
                    }
                };
                g.grid.stream().flatMap(List::stream).forEach(makeElementPull);
            }
        }
    }
}
