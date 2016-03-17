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
        if (length == 3) {
            for (int yRow = 1; yRow <= length; ++yRow) {
                for (int xCol = 1; xCol <= length; ++xCol) {
                    Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
                    Tile tile = new Tile(this, pair);
                    this.tile = tile; // any Tile instance works here
                    boardPanel.add(tile);
                }
            }
        }

        else if (length == 10) {

            int tileNumber;
            for (int yRow = 1; yRow <= length; ++yRow) {
                for (int xCol = 1; xCol <= length; ++xCol) {
                    Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
                    Tile tile = new Tile(this, pair);
                    this.tile = tile; // any Tile instance works here
                    boardPanel.add(tile);
                    tileNumber = coordToNumber(length, xCol, yRow);
                    if (tileNumber == 1  || tileNumber ==  38) { tile.setBackground(new Color(  0, 128,   0)); }
                    if (tileNumber == 4  || tileNumber ==  14) { tile.setBackground(new Color(  0, 128,  64)); }
                    if (tileNumber == 9  || tileNumber ==  31) { tile.setBackground(new Color( 64, 128, 128)); }
                    if (tileNumber == 21 || tileNumber ==  42) { tile.setBackground(new Color(  0, 128, 192)); }
                    if (tileNumber == 28 || tileNumber ==  84) { tile.setBackground(new Color(  0, 128, 255)); }
                    if (tileNumber == 36 || tileNumber ==  44) { tile.setBackground(new Color(  0, 255, 255)); }
                    if (tileNumber == 51 || tileNumber ==  67) { tile.setBackground(new Color( 27, 128, 228)); }
                    if (tileNumber == 71 || tileNumber ==  91) { tile.setBackground(new Color( 64, 191, 118)); }
                    if (tileNumber == 80 || tileNumber == 100) { tile.setBackground(new Color( 68, 187, 181)); }

                    if (tileNumber == 16 || tileNumber ==   6) { tile.setBackground(new Color(255, 128, 192)); }
                    if (tileNumber == 47 || tileNumber ==  26) { tile.setBackground(new Color(255,   0, 255)); }
                    if (tileNumber == 49 || tileNumber ==  11) { tile.setBackground(new Color(255, 128, 255)); }
                    if (tileNumber == 56 || tileNumber ==  53) { tile.setBackground(new Color(128,   0,  64)); }
                    if (tileNumber == 62 || tileNumber ==  19) { tile.setBackground(new Color(128,   0, 255)); }
                    if (tileNumber == 64 || tileNumber ==  60) { tile.setBackground(new Color(255,   0, 128)); }
                    if (tileNumber == 87 || tileNumber ==  24) { tile.setBackground(new Color(238,  94,  68)); }
                    if (tileNumber == 93 || tileNumber ==  73) { tile.setBackground(new Color(225, 164,  81)); }
                    if (tileNumber == 95 || tileNumber ==  75) { tile.setBackground(new Color(243, 121,  63)); }
                    if (tileNumber == 98 || tileNumber ==  78) { tile.setBackground(new Color(201,  29,  72)); }

                }
            }
        }




        add(boardPanel,BorderLayout.CENTER);
        turnLabel = new JLabel();
        turnLabel.setVerticalAlignment(JLabel.BOTTOM);
        turnPanel.add(turnLabel);
        add(turnPanel,BorderLayout.SOUTH);
    }

    private int coordToNumber(int boardLength, int xCol, int yRow) {
        int numRowsBelow = boardLength - yRow;
        int partialSum = numRowsBelow * boardLength;
        if (numRowsBelow % 2 == 0) {
            return partialSum + xCol;
        } else {
            return partialSum + (boardLength - xCol + 1);
        }
    }

    private void setTurnLabel(String text)
    {
    	turnLabel.setText(text);
    }

    public void handleRequest(Request request) {
        String[] tokens = request.getTokens();
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
            case MOVE_TO:
                if (tokens[1].equals(username)) {
                    setTurnLabel(Command.MOVE_TO.getMessage2());
                } else {
                    setTurnLabel(Command.MOVE_TO.getMessage1());
                }
                tile.handleRequest(request);
                break;
            case REMOVE_FROM:
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
