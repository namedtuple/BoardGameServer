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
    //private HashMap<Pair<Integer, Integer>, GUIBoardCell> guiBoardMap;
    private HashMap<Pair<Integer, Integer>, JButton> guiBoardMap;
    private HashMap<Character, BufferedImage> imageHashMap;
    private HashMap<Pair<Integer, Integer>, Character> boardMap;

    // Methods
    public GUI(Board board) {
        this.board = board;
        frame = new JFrame("GUI");
        boardPanel = new JPanel();
        guiBoardMap = new HashMap<>();
        imageHashMap = new HashMap<>();
        boardMap = board.getBoardMap();
        loadImages();
        //setupGUIBoard(this.board.getBoardMap());
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
                BoardButtonAction action = new BoardButtonAction(board);
                ImageIcon icon = action.chooseIcon(board.getValue(pair));
                BoardButton button = new BoardButton(pair, icon);
                button.addActionListener(action);
                guiBoardMap.put(pair, button);
                boardPanel.add(button);
            }
        }
    }

    //public void setupGUIBoard(HashMap<Pair<Integer, Integer>, Character> boardMap) {
    //    for (int xCol=1; xCol<=board.getSize(); ++xCol) {
    //        for (int yRow=1; yRow<=board.getSize(); ++yRow) {
    //            Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
    //            GUIBoardCell cell = new GUIBoardCell();
    //            guiBoardMap.put(pair, cell);
    //            boardPanel.add(cell);
    //            cell.setLayout(new GridBagLayout());
    //
    //            Character c = boardMap.get(Pair.with(xCol, yRow));
    //            BufferedImage img = imageHashMap.get(c);
    //            JLabel label = new JLabel(new ImageIcon(img));
    //
    //            cell.add(label);
    //        }
    //    }
    //}
    //
    //public void update() {
    //    for (int xCol=1; xCol<=board.getSize(); ++xCol) {
    //        for (int yRow = 1; yRow <= board.getSize(); ++yRow) {
    //            Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
    //            Character c = boardMap.get(pair);
    //            GUIBoardCell cell = guiBoardMap.get(pair);
    //            JLabel label = (JLabel) cell.getComponent(0);
    //            label.setIcon(new ImageIcon(imageHashMap.get(c)));
    //        }
    //    }
    //}
}
