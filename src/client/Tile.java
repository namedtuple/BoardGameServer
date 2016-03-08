package client;
import org.javatuples.Pair;

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
    private static Board board;
    private static HashMap<Character, ImageIcon> imageHashMap;
    private static List<Tile> instances = new ArrayList<>();
    private static Tile lastClickedTile;
    private Pair<Integer, Integer> coordinates; //saves button coordinates

    // Methods
    public Tile(Board board, Pair<Integer, Integer> coordinates) {
        Tile.board = board;
        if (imageHashMap == null) {
            loadImages();
        }
        Tile.instances.add(this);
        this.coordinates = coordinates;
        this.addActionListener(this);
    }

    private static void loadImages() {
        imageHashMap = new HashMap<>();
        try {
            imageHashMap.put('X', new ImageIcon(ImageIO.read(new File("img/x.png"))));
            imageHashMap.put('O', new ImageIcon(ImageIO.read(new File("img/o.png"))));
            imageHashMap.put('_', new ImageIcon(ImageIO.read(new File("img/blank.png"))));
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
        lastClickedTile = this;
        handleRequest("MOVE " + coordinates);
    }

    // Returns an ImageIcon for the X or O icon
    private static ImageIcon chooseIcon(char piece) {
        return imageHashMap.get(piece);
    }

    private static Tile getTile(Pair<Integer, Integer> location) {
        Tile returnTile = null;
        for (Tile tile : instances) {
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
        if (request.startsWith("MOVE")) {
            board.handleRequest(request);
        }
        else if (request.startsWith("VALID_MOVE")) {
            lastClickedTile.setIcon(chooseIcon(board.getID()));
            lastClickedTile.setDisabledIcon(chooseIcon(board.getID()));
            lastClickedTile.setEnabled(false);
        }
        else if (request.startsWith("OPPONENT_MOVED")) {
            Tile tile = getTile(extractPosition(request));
            tile.setIcon(chooseIcon(board.getOpponentID()));
            tile.setDisabledIcon(chooseIcon(board.getOpponentID()));
            tile.setEnabled(false);
        }
    }
}
