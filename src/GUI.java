import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    // Fields
    private Board board;
    private Client client;
    private int numTitleChanges = 0;
    private String baseTitle;

    // Methods
    public GUI(Client client, String title) {
        super(title);
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

    @Override
    public void setTitle(String title) {
        System.out.println("setTitle(): " + title);
        if (numTitleChanges < 1) {
            baseTitle = title;
        }
        ++numTitleChanges;
        super.setTitle(title);
    }

    public void appendToTitle(String toAppend) {
        setTitle(baseTitle + ": " + toAppend);
    }

}
