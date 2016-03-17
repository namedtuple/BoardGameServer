package client;
import org.javatuples.Pair;
import shared.Command;
import shared.Request;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tile extends JButton implements ActionListener {

    // Fields
    private static BoardScreen boardScreen;
    private static HashMap<String, ImageIcon> imageHashMap;
    private static List<Tile> instances = new ArrayList<>();
    private static Tile lastClickedTile;
    private static String username;
    private static String opponentUsername;
    private Pair<Integer, Integer> coordinates; //saves button coordinates

    // Methods
    public Tile(BoardScreen boardScreen, Pair<Integer, Integer> coordinates) {
        Tile.boardScreen = boardScreen;
        Tile.instances.add(this);
        this.coordinates = coordinates;
        this.addActionListener(this);
    }

    private static void loadImages(String usernamePlayer1, String usernamePlayer2) {
        Tile.imageHashMap = new HashMap<>();
        try {
            Tile.imageHashMap.put(usernamePlayer1, new ImageIcon(ImageIO.read(new File("img/x.png"))));
            Tile.imageHashMap.put(usernamePlayer2, new ImageIcon(ImageIO.read(new File("img/o.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * This will execute when a button object is clicked on the game board.
     * Gameboard will be properly updated to what is clicked as well.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Tile.lastClickedTile = this;
        handleRequest(new Request(Command.MOVE, username + " " + coordinates.toString()));
    }

    private static ImageIcon chooseIcon(String piece) {
        return Tile.imageHashMap.get(piece);
    }

    private static Tile getTile(Pair<Integer, Integer> location) {
        Tile returnTile = null;
        for (Tile tile : Tile.instances) {
            if (tile.getCoordinates().equals(location)) {
                returnTile = tile;
                break;
            }
        }
        return returnTile;
    }

    private Pair<Integer, Integer> getCoordinates() {
        return coordinates;
    }

    // Helper method to obtain position Pair from received String message
    @SuppressWarnings("Duplicates")
    private static Pair<Integer, Integer> extractPosition(Request request) {
        String message = request.getRequest();
        int i = message.indexOf('[');
        int j = message.indexOf(',');
        int k = message.indexOf(']');
        int x = Integer.parseInt(message.substring(i+1, j).trim());
        int y = Integer.parseInt(message.substring(j+1, k).trim());
        return Pair.with(x, y);
    }

    public void handleRequest(Request request) {
        String[] tokens = request.getTokens();
        Command command = request.getCommand();
        switch (command) {
            case MOVE:
                Tile.boardScreen.handleRequest(request);
                break;
            case NEW_GAME:
                String player1 = tokens[2];
                String player2 = tokens[4];
                loadImages(player1, player2);
                if (player1.equals(boardScreen.getUsername())) { // then i'm p1 and I should be X
                    Tile.username = player1;
                    Tile.opponentUsername = player2;
                } else { // then i'm p2 and I should be O
                    Tile.username = player2;
                    Tile.opponentUsername = player1;
                }
                break;
            case VALID_MOVE:
                Tile.lastClickedTile.setIcon(chooseIcon(Tile.username));
                Tile.lastClickedTile.setDisabledIcon(chooseIcon(Tile.username));
                Tile.lastClickedTile.setEnabled(false);
                break;
            case OPPONENT_MOVED:
                Tile tile = getTile(extractPosition(request));
                tile.setIcon(chooseIcon(Tile.opponentUsername));
                tile.setDisabledIcon(chooseIcon(Tile.opponentUsername));
                tile.setEnabled(false);
                break;
            case VICTORY: case DEFEAT: case TIE:
                Tile.boardScreen = null;
                Tile.imageHashMap = null;
                Tile.instances = new ArrayList<>();
                Tile.lastClickedTile = null;
                break;
            default:
                break;
        }
    }

}
