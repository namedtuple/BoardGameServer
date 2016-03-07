import org.javatuples.Pair;

import javax.swing.*;

public class Board extends JPanel {

    // Fields
    private GUI gui;
    private int length;     // size of board
    private char ID;
    private Tile tile;

    // Methods
    public Board(GUI gui, int length) {
        this.gui = gui;
        this.length = length;
        setupBoard();
    }

    /*
     * This method will create a hashmap with keys being a button's coordinates,
     * and the values are Button objects. Buttons are also added to the panel
     * at the end of this method.
     */
    private void setupBoard() {
        for (int yRow = 1; yRow <= length; ++yRow) {
            for (int xCol = 1; xCol <= length; ++xCol) {
                Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
                Tile tile = new Tile(this, pair);
                this.tile = tile; // any Tile instance works here
                add(tile);
            }
        }
    }

    public char getID() {
        return ID;
    }

    public char getOpponentID() {
        return ID == 'X' ? 'O' : 'X';
    }

    public void handleRequest(String request) {
        if (request.startsWith("MOVE")) {
            gui.handleRequest(request);
        }
        else if (request.startsWith("WELCOME")) {
            ID = request.charAt(8);
        }
        else if (request.startsWith("VALID_MOVE")){
            tile.handleRequest(request);
        }
        else if (request.startsWith("OPPONENT_MOVED")) {
            tile.handleRequest(request);
        }
    }

}
