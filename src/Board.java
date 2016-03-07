import org.javatuples.Pair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel {

    // Fields
    private GUI gui;
    private int length;     // size of board
    private List<Tile> slaves;
    private char ID;
    private Tile lastClickedTile;

    // Methods
    public Board(GUI gui, int length) {
        this.gui = gui;
        this.length = length;
        this.slaves = new ArrayList<>();
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
                add(tile);
                slaves.add(tile);
            }
        }
    }

    public GUI getMaster() {
        return gui;
    }

    public Tile getSlave(Pair<Integer, Integer> location) {
        Tile returnTile = null;
        for (Tile tile : slaves) {
            if (tile.getCoordinates().equals(location)) {
                returnTile = tile;
                break;
            }
        }
        return returnTile;
    }

    public char getID() {
        return ID;
}

    public void setID(char ID) {
        this.ID = ID;
    }

    public char getOpponentID() {
        return ID == 'X' ? 'O' : 'X';
    }

    public Tile getLastClickedTile() {
        return lastClickedTile;
    }

    public void setLastClickedTile(Tile lastClickedTile) {
        this.lastClickedTile = lastClickedTile;
    }

}
