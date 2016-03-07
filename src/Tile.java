import org.javatuples.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Tile extends JButton implements ActionListener {

    // Fields
    private static Board board;
    private static HashMap<Character, ImageIcon> imageHashMap;
    private Pair<Integer, Integer> coordinates; //saves button coordinates

    // Methods
    public Tile(Board board, Pair<Integer, Integer> coordinates) {
        Tile.board = board;
        if (imageHashMap == null) {
            loadImages();
        }
        this.coordinates = coordinates;
        this.addActionListener(this);
    }

    // Constructor helpful for debugging
    public Tile(Board board, Pair<Integer, Integer> coordinates, Color color) {
        Tile.board = board;
        this.coordinates = coordinates;
        this.addActionListener(this);
        this.setBackground(color);
    }

    public static void loadImages() {
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
        getMaster().setLastClickedTile(this);
        getMaster().getMaster().getMaster().send("MOVE " + coordinates);
    }

    public Board getMaster() {
        return board;
    }

    public Pair<Integer, Integer> getCoordinates() {
        return coordinates;
    }

    public void setIcon() {
        char ID = board.getID();
        this.setIcon(chooseIcon(ID));
        this.setDisabledIcon(chooseIcon(ID));
        this.setEnabled(false);
    }

    public void setOpponentIcon() {
        char ID = board.getOpponentID();
        this.setIcon(chooseIcon(ID));
        this.setDisabledIcon(chooseIcon(ID));
        this.setEnabled(false);
    }

    // Returns an ImageIcon for the X or O icon
    public ImageIcon chooseIcon(char piece) {
        return imageHashMap.get(piece);
    }
}
