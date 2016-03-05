import org.javatuples.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GUI {

    // Fields
    private JFrame frame;
    private JPanel boardPanel;
    private Board board;
    private Client client;
    private HashMap<Pair<Integer, Integer>, JButton> guiBoardMap;
    private HashMap<Character, BufferedImage> imageHashMap;

    // Methods
    public GUI(Board board, Client client) {
        this.board = board;
        frame = new JFrame("GUI");
        boardPanel = new JPanel();
        guiBoardMap = new HashMap<>();
        imageHashMap = new HashMap<>();
        loadImages();
        createBoard();

        boardPanel.setBackground(Color.yellow);
        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));

        frame.add(boardPanel, "Center");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 300);
        frame.setVisible(true);
        frame.setResizable(true);
    }

    public void loadImages() {
        try {
            imageHashMap.put('X', ImageIO.read(new File("img/x.png")));
            imageHashMap.put('O', ImageIO.read(new File("img/o.png")));
            imageHashMap.put('_', ImageIO.read(new File("img/blank.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * This method will create a hashmap with keys being a button's coordinates,
     * and the values are Button objects. Buttons are also added to the panel
     * at the end of this method.
     */
    public void createBoard() {
        guiBoardMap = new HashMap<>();
        for (int xCol = 1; xCol <= board.getSize(); ++xCol) {
            for (int yRow = 1; yRow <= board.getSize(); ++yRow) {
                Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
                TileAction action = new TileAction(board, client);
                ImageIcon icon = action.chooseIcon(board.getValue(pair));
                Tile button = new Tile(pair, icon);
                button.addActionListener(action);
                guiBoardMap.put(pair, button);
                boardPanel.add(button);
            }
        }
    }
}
