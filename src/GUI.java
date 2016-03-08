import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    // Fields
    private Client client;
    private Board board;
    private int numTitleChanges = 0;
    private String baseTitle;

    // Methods
    public GUI(Client client, String title) {
        super(title);
        this.client = client;

        int sz = 10;
        this.board = new Board(this, sz);

        board.setBackground(Color.yellow);
        board.setLayout(new GridLayout(sz, sz, 2, 2));
        add(board, "Center");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(450, 300);
        setVisible(true);
        setResizable(true);
    }

    @Override
    public void setTitle(String title) {
        if (numTitleChanges < 1) {
            baseTitle = title;
        }
        ++numTitleChanges;
        super.setTitle(title);
    }

    public void appendToTitle(String toAppend) {
        setTitle(baseTitle + " - \"" + toAppend + "\"");
    }

    public void handleRequest(String request) {
        if (request.startsWith("MOVE")) {
            client.handleRequest(request);
        }
        else if (request.startsWith("WELCOME")) {
            setTitle("" + request.charAt(8));
            board.handleRequest(request);
        }
        else if (request.startsWith("VALID_MOVE")){
            appendToTitle("Opponent's turn");
            board.handleRequest(request);
        }
        else if (request.startsWith("OPPONENT_MOVED")) {
            appendToTitle("Your turn");
            board.handleRequest(request);
        }
        else if (request.startsWith("VICTORY")) {
            appendToTitle("You win");
        }
        else if (request.startsWith("DEFEAT")) {
            appendToTitle("You lose");
        }
        else if (request.startsWith("TIE")) {
            appendToTitle("You tie");
        }
        else if (request.startsWith("MESSAGE")) {
            appendToTitle(request.substring(8));
        }
        else {
            board.handleRequest(request);
        }
    }

}
