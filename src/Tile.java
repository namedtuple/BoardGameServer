import org.javatuples.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Tile extends JButton implements ActionListener {

    // Fields
    private Board board;
    private Pair<Integer, Integer> coordinates; //saves button coordinates
    private HashMap<Character, ImageIcon> imageHashMap;

    // Methods
    public Tile(Board board, Pair<Integer, Integer> coordinates) {
        this.board = board;
        this.coordinates = coordinates;
        loadImages();
        this.addActionListener(this);
    }

    public void loadImages() {
        imageHashMap = new HashMap<>();
        try {
            imageHashMap.put('X', new ImageIcon(ImageIO.read(new File("img/x.png"))));
            imageHashMap.put('O', new ImageIcon(ImageIO.read(new File("img/o.png"))));
            imageHashMap.put('_', new ImageIcon(ImageIO.read(new File("img/blank.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    /*
     * This will execute when a button object is clicked on the game board.
     * Gameboard will be properly updated to what is clicked as well.
     */
    public void actionPerformed(ActionEvent e) {
        System.out.println("You clicked '" + getCoordinates().toString() + "'");
        //client.send("You clicked " + getCoordinates().toString());
        getMaster().setLastClickedTile(this);
        getMaster().getMaster().getMaster().send("MOVE " + coordinates);
    }

    // Helper method that will return an X or O icon
    public ImageIcon chooseIcon(char piece) {
        return imageHashMap.get(piece);
    }

    public Pair<Integer, Integer> getCoordinates() {
        return coordinates;
    }

    public Board getMaster() {
        return board;
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
}
