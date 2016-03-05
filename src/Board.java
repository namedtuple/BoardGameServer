import org.javatuples.Pair;

import javax.swing.*;
import java.util.HashMap;

public class Board extends JPanel {

    // Fields
    private Client client;
    private HashMap<Pair<Integer, Integer>, Character> guiBoardMap;
    private int length;       // size of board
    private int turn;       // 0 is player one turn, 1 is player two turn

    // Methods
    public Board(Client client, int length) {
        this.client = client;
        this.length = length;
        setupBoard();
    }

    /*
     * This method will create a hashmap with keys being a button's coordinates,
     * and the values are Button objects. Buttons are also added to the panel
     * at the end of this method.
     */
    private void setupBoard() {
        guiBoardMap = new HashMap<>();
        for (int xCol = 1; xCol <= length; ++xCol) {
            for (int yRow = 1; yRow <= length; ++yRow) {
                Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
                guiBoardMap.put(pair, '_');
                Tile tile = new Tile(client, this, pair);
                add(tile);
            }
        }

    }

    public void makeMove(Pair<Integer, Integer> location, char piece) {
        guiBoardMap.put(location, piece);
    }

    public void makeMove(int xCol, int yRow, char piece) {
        guiBoardMap.put(Pair.with(xCol, yRow), piece);
    }

    public void debugPrintBoardContentsBetter() {
        System.out.print("\n\n");
        for (int xCol = 1; xCol <= length; ++xCol) {
            for (int yRow = 1; yRow <= length; ++yRow) {
                char val = guiBoardMap.get(Pair.with(xCol, yRow));
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    public int getLength() {
        return length;
    }

    public HashMap<Pair<Integer, Integer>, Character> getGuiBoardMap() {
        return guiBoardMap;
    }

    public char getValue(Pair<Integer, Integer> pair) {
        return guiBoardMap.get(pair);
    }

    public Pair<Integer, Integer> getKey(char value) {
        for (Pair<Integer, Integer> pair : guiBoardMap.keySet()) {
            if (guiBoardMap.get(pair).equals(value)) {
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
