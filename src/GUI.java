import org.javatuples.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class GUI {

    // Fields
    private JFrame frame;
    private JPanel boardPanel;
    private Board board;
    private HashMap<Pair<Integer, Integer>, GUIBoardCell> guiBoardMap;
    private HashMap<Pair<Integer, Integer>, Character> boardMap;

    // Methods
    public GUI(Board board) {
        this.board = board;
        boardMap = board.getBoardMap();
        frame = new JFrame("GUI");
        boardPanel = new JPanel();
        guiBoardMap = new HashMap<>();

        boardPanel.setBackground(Color.yellow);
        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));

        frame.add(boardPanel, "Center");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 300);
        frame.setVisible(true);
        frame.setResizable(false);

        update();
    }

    public void update() {
        for (int xCol=1; xCol<=board.getSize(); ++xCol) {
            for (int yRow=1; yRow<=board.getSize(); ++yRow) {
                Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
                GUIBoardCell cell = new GUIBoardCell();
                guiBoardMap.put(pair, cell);
                boardPanel.add(cell);
                cell.setLayout(new GridBagLayout());

                Character c = boardMap.get(Pair.with(xCol, yRow));
                JLabel label = new JLabel(String.valueOf(c));

                GridBagConstraints gbc = new GridBagConstraints();
                cell.add(label, gbc);
            }
        }
    }
}
