package client;
import org.javatuples.Pair;
import shared.Command;
import shared.Request;

import javax.swing.*;
import java.awt.*;

public class BoardScreen extends JPanel {

    // Fields
    private GUI gui;
    private int length;     // size of board
    private Tile tile;
    private JLabel turnLabel;
    private JPanel boardPanel, turnPanel;
    private String username;

    // Methods
    public BoardScreen(GUI gui, int length, String username) {
        this.gui = gui;
        this.length = length;
        this.username = username;
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

    private void setTurnLabel(String text)
    {
    	turnLabel.setText(text);
    }

    public void handleRequest(Request request) {
        Command command = request.getCommand();
        switch (command) {
            case MOVE:
                gui.handleRequest(request);
                break;
            case NEW_GAME:
                setTurnLabel("Player X starts first"); // TODO
                tile.handleRequest(request);
                break;
            case VALID_MOVE: case OPPONENT_MOVED: case VICTORY: case DEFEAT: case TIE:
                setTurnLabel(command.getMessage());
                tile.handleRequest(request);
                break;
            case MOVE_TO: case REMOVE_FROM:
                tile.handleRequest(request);
                break;
            default:
                break;
        }
    }

    public String getUsername() {
        return username;
    }

}
