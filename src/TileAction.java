import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TileAction implements ActionListener {

    // Fields
    private Board board;
    private Client client;
    private HashMap<Character, ImageIcon> imageHashMap;

    // Methods
    public TileAction(Board board, Client client) {
        this.board = board;
        this.client = client;
        loadImages();
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
     */ public void actionPerformed(ActionEvent e) {
        Tile command = (Tile) e.getSource();
        char iconChar = board.getTurn() == 0 ? 'X' : 'O';
        command.setIcon(chooseIcon(iconChar));
        command.setDisabledIcon(chooseIcon(iconChar));
        command.setEnabled(false);
        board.makeMove(command.getCoordinates(), iconChar);
        board.debugPrintBoardContents();
        board.debugPrintBoardContentsBetter();
        board.switchTurn();
    }

    // Helper method that will return an X or O icon
    public ImageIcon chooseIcon(char piece) {
        return imageHashMap.get(piece);
    }
}
