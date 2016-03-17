package games;

import org.javatuples.Pair;

import java.util.Collection;
import java.util.HashMap;


public class Board {

    // Fields
    private HashMap<Pair<Integer, Integer>, Cell> boardMap;

    // Methods
    public Board(int length) {
        boardMap = new HashMap<>();
        for (int xCol = 1; xCol <= length; ++xCol) {
            for (int yRow = 1; yRow <= length; ++yRow) {
                Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
                boardMap.put(pair, new Cell(pair));
            }
        }
    }

    public Cell getCell(Pair<Integer, Integer> location) {
        return boardMap.get(location);
    }

    public Collection<Cell> getCells() {
        return boardMap.values();
    }


}
