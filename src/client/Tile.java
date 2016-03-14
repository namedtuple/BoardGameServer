package client;
import org.javatuples.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Tile extends JButton implements ActionListener {

    // Fields
    private static Board board;
    private static HashMap<String, ImageIcon> imageHashMap;
    private static List<Tile> instances = new ArrayList<>();
    private static Tile lastClickedTile;
    private Pair<Integer, Integer> coordinates; //saves button coordinates

    // Methods
    public Tile(Board board, Pair<Integer, Integer> coordinates) {
        Tile.board = board;
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
        handleRequest("MOVE " + coordinates);
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
    private static Pair<Integer, Integer> extractPosition(String message) {
        int i = message.indexOf('[');
        int j = message.indexOf(',');
        int k = message.indexOf(']');
        int x = Integer.parseInt(message.substring(i+1, j).trim());
        int y = Integer.parseInt(message.substring(j+1, k).trim());
        return Pair.with(x, y);
    }

    public void handleRequest(String request) {
        String[] splitRequest = request.split(" ");
        String firstToken = splitRequest[0];
        if (request.startsWith("MOVE")) {
            Tile.board.handleRequest(request);
        }
        else if (request.startsWith("WELCOME")) {
            if (Tile.imageHashMap == null) {
                if (request.indexOf(" X ") < request.indexOf(" O ")) {
                    loadImages(splitRequest[1], splitRequest[3]);
                } else {
                    loadImages(splitRequest[3], splitRequest[1]);
                }
            }
        }
        else if (request.startsWith("VALID_MOVE")) {
            Tile.lastClickedTile.setIcon(chooseIcon(Tile.board.getUsername()));
            Tile.lastClickedTile.setDisabledIcon(chooseIcon(Tile.board.getUsername()));
            Tile.lastClickedTile.setEnabled(false);
        }
        else if (request.startsWith("OPPONENT_MOVED")) {
            Tile tile = getTile(extractPosition(request));
            tile.setIcon(chooseIcon(Tile.board.getOpponentUsername()));
            tile.setDisabledIcon(chooseIcon(Tile.board.getOpponentUsername()));
            tile.setEnabled(false);
        }
        else if (Arrays.asList("VICTORY, DEFEAT, TIE".split(", ")).contains(firstToken)) {
            Tile.board = null;
            Tile.imageHashMap = null;
            Tile.instances = new ArrayList<>();
            Tile.lastClickedTile = null;
        }
    }
}
