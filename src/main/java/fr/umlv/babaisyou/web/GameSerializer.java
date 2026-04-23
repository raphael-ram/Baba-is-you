package fr.umlv.babaisyou.web;

import fr.umlv.babaisyou.model.*;
import fr.umlv.babaisyou.web.dto.GameStateDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class GameSerializer {

    public GameStateDto serialize(Grid grid, String status, int level) {
        int rows = grid.getNbLines();
        int cols = grid.getNbColumns();
        List<List<List<String>>> gridDto = new ArrayList<>(rows);

        for (int r = 0; r < rows; r++) {
            List<List<String>> rowDto = new ArrayList<>(cols);
            for (int c = 0; c < cols; c++) {
                LinkedList<Cell> stack = grid.grid.get(r).get(c);
                List<String> cellDto = new ArrayList<>(stack.size());
                // Iterate bottom to top so the frontend draws in correct order
                var iter = stack.descendingIterator();
                while (iter.hasNext()) {
                    cellDto.add(cellToString(iter.next()));
                }
                rowDto.add(cellDto);
            }
            gridDto.add(rowDto);
        }
        return new GameStateDto(status, level, rows, cols, gridDto);
    }

    private String cellToString(Cell cell) {
        return switch (cell) {
            case Material m -> "ENTITY_" + m.property().toUpperCase();
            case Word w     -> "WORD_" + w.property().toUpperCase();
            case Action a   -> "WORD_" + a.property().toUpperCase();
            case Operator o -> "WORD_IS";
            case Element e  -> "EMPTY";
            default         -> "EMPTY";
        };
    }
}
