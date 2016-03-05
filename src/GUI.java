import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    // Fields
    private Board board;
    private Client client;

    // Methods
    public GUI(Client client) {
        this.client = client;
        this.board = new Board(client, 3);

        board.setBackground(Color.yellow);
        board.setLayout(new GridLayout(3, 3, 2, 2));

        add(board, "Center");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setVisible(true);
        setResizable(true);
    }

}
