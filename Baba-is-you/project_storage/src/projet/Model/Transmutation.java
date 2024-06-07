package projet.Model;

import java.util.List;

public class Transmutation {
    private Grid grid;

    public Transmutation(Grid grid) {
        this.grid = grid;
    }

    /**
     * Transforms all elements of a certain type into another type.
     * 
     * @param from the name of the elements to transform
     * @param to   the name of the elements to transform into
     */
    public void transform(String from, String to) {
        grid.grid.stream().flatMap(List::stream).forEach(cellList -> {
            if (!cellList.isEmpty() && cellList.getFirst().property().equals(from) && !(cellList.getFirst() instanceof Word)) {
                Cell transformedCell = new Material(to, cellList.getFirst().getPositionX(),
                        cellList.getFirst().getPositionY());
                cellList.clear();
                cellList.addFirst(transformedCell);
            }
        });
    }
}
