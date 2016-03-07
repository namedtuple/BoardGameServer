import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    // Fields
    private Board board;
    private Client client;

    // Methods
    public GUI(Client client, String name) {
        super(name);
        this.client = client;
        this.board = new Board(this, 3);

        board.setBackground(Color.yellow);
        board.setLayout(new GridLayout(3, 3, 2, 2));
        add(board, "Center");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(450, 300);
        setVisible(true);
        setResizable(false);
    }

    public Client getMaster() {
        return client;
    }

    public Board getSlave() {
        return board;
    }

}
