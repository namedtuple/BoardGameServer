import org.javatuples.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class GUI {

    // Fields
    private JFrame jFrame;
    private Board board;
    private HashMap<Pair<Integer, Integer>, GUIBoardCell> guiBoardMap;

    // Methods
    public GUI(Board board) {
        this.board = board;
        this.jFrame = new JFrame("GUI");

        JPanel boardPanel = new JPanel();
        boardPanel.setBackground(Color.yellow);
        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));

        guiBoardMap = new HashMap<>();
        for (int xCol=1; xCol<=board.getSize(); ++xCol) {
            for (int yRow=1; yRow<=board.getSize(); ++yRow) {
                Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
                GUIBoardCell cell = new GUIBoardCell();
                guiBoardMap.put(pair, cell);
                boardPanel.add(cell);
            }
        }

        jFrame.add(boardPanel, "Center");

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(450, 300);
        jFrame.setVisible(true);
        jFrame.setResizable(false);

    }
}
