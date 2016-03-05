import org.javatuples.Pair;

import javax.swing.*;
import java.awt.*;

public class GUI {

    // Fields
    private JFrame frame;
    private JPanel boardPanel;
    private Board board;
    private Client client;

    // Methods
    public GUI(Client client) {
        this.client = client;
        this.board = new Board(3);
        frame = new JFrame("GUI");
        boardPanel = new JPanel();
        createBoard();

        boardPanel.setBackground(Color.yellow);
        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));

        frame.add(boardPanel, "Center");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 300);
        frame.setVisible(true);
        frame.setResizable(true);
    }

    /*
     * This method will create a hashmap with keys being a button's coordinates,
     * and the values are Button objects. Buttons are also added to the panel
     * at the end of this method.
     */
    public void createBoard() {
        for (int xCol = 1; xCol <= board.getSize(); ++xCol) {
            for (int yRow = 1; yRow <= board.getSize(); ++yRow) {
                Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
                Tile tile = new Tile(client, board, pair);
                boardPanel.add(tile);
            }
        }
    }
}
