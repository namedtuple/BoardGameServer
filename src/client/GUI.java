package client;
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
        this.board = new Board(this, 3);

        add(board, "Center");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(450, 300);
        setVisible(true);
        setResizable(false);
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
            board.setTurnLabel("Player X starts first");
            board.handleRequest(request);
        }
        else if (request.startsWith("VALID_MOVE")){
            board.setTurnLabel("It is the opponent's turn");
            board.handleRequest(request);
        }
        else if (request.startsWith("OPPONENT_MOVED")) {
            board.setTurnLabel("It is your turn");
            board.handleRequest(request);
        }
        else if (request.startsWith("VICTORY")) {
            board.setTurnLabel("You win!");
        }
        else if (request.startsWith("DEFEAT")) {
            board.setTurnLabel("You lose!");
        }
        else if (request.startsWith("TIE")) {
            board.setTurnLabel("You tied!");
        }
        else if (request.startsWith("MESSAGE")) {
            appendToTitle(request.substring(8));
        }
        else {
            board.handleRequest(request);
        }
    }

}
