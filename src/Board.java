import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;

public class Board {

    // Fields
    private HashMap<Pair<Integer, Integer>, Character> boardMap;
    private int size;       // size of board
    private int turn;       // 0 is player one turn, 1 is player two turn

    // Methods
    public Board(int size) {
        this.size = size;
        boardMap = new HashMap<>();
        for (int xCol = 1; xCol <= size; ++xCol) {
            for (int yRow = 1; yRow <= size; ++yRow) {
                Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
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

    public void debugPrintBoardContentsBetter() {
        System.out.print("\n\n");
        for (int xCol = 1; xCol <= size; ++xCol) {
            for (int yRow = 1; yRow <= size; ++yRow) {
                char val = boardMap.get(Pair.with(xCol, yRow));
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    public int getSize() {
        return size;
    }

    public HashMap<Pair<Integer, Integer>, Character> getBoardMap() {
        return boardMap;
    }

    public char getValue(Pair<Integer, Integer> pair) {
        return boardMap.get(pair);
    }

    public Pair<Integer, Integer> getKey(char value) {
        for (Pair<Integer, Integer> pair : boardMap.keySet()) {
            if (boardMap.get(pair).equals(value)) {
                return pair;
            }
        }
        return null;
    }

    public int getTurn() {
        return turn;
    }

    public void switchTurn() {
        turn = ((turn == 0) ? 1 : 0);
    }
}
