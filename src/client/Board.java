package client;
import org.javatuples.Pair;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {

    // Fields
    private GUI gui;
    private int length;     // size of board
    private String username;
    private String opponentUsername;
    private Tile tile;
    private JLabel turnLabel;
    private JPanel boardPanel, turnPanel;

    // Methods
    public Board(GUI gui, int length) {
        this.gui = gui;
        this.length = length;
        setBackground(Color.yellow);
        setLayout(new BorderLayout());
        //JPanel for the game board
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(length, length, 2, 2));
        //JPanel for the turn label
        turnPanel = new JPanel();
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
                boardPanel.add(tile);
            }
        }
        add(boardPanel,BorderLayout.CENTER);
        turnLabel = new JLabel();
        turnLabel.setVerticalAlignment(JLabel.BOTTOM);
        turnPanel.add(turnLabel);
        add(turnPanel,BorderLayout.SOUTH);
    }

    public String getUsername() {
        return username;
    }

    public String getOpponentUsername() {
        return opponentUsername;
    }

    public void setTurnLabel(String text)
    {
    	turnLabel.setText(text);
    }

    public void handleRequest(String request) {
        String[] splitRequest = request.split(" ");

        if (request.startsWith("MOVE")) {
            gui.handleRequest(request);
        }
        else if (request.startsWith("WELCOME")) {
            username = splitRequest[1];
            opponentUsername = splitRequest[3];
            tile.handleRequest(request);
        }
        else if (request.startsWith("VALID_MOVE")){
            tile.handleRequest(request);
        }
        else if (request.startsWith("OPPONENT_MOVED")) {
            tile.handleRequest(request);
        }
    }

}
