import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;


public class Board {

    // Fields
    private HashMap<Pair, Character> boardMap;
    private int size;

    // Methods
    public Board(int size) {
        this.size = size;
        boardMap = new HashMap<>();
        for (int xCol=1; xCol<=size; ++xCol) {
            for (int yRow=1; yRow<=size; ++yRow) {
                Pair<Integer, Integer> pair = Pair.with(xCol,yRow);
                boardMap.put(pair, '_');
            }
        }
    }

    public void makeMove(Pair<Integer, Integer> location, char piece) {
        boardMap.put(location, piece);
    }

    public void makeMove(int xCol, int yRow, char piece) {
        boardMap.put(Pair.with(xCol, yRow), piece);
    }

    public void debugPrintBoardContents() {
        for (Map.Entry<Pair, Character> entry : boardMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        System.out.println();
    }

    public void debugPrintBoardContentsBetter() {
        for (int xCol = 1; xCol <= size; ++xCol) {
            for (int yRow = 1; yRow <= size; ++yRow) {
                char val = boardMap.get(Pair.with(xCol, yRow));
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

}
