package client;
import javax.swing.*;

public class GUI extends JFrame {

    // Fields
    private Client client;
    private LoginScreen loginScreen;
    private LobbyScreen lobbyScreen;
    private Board board;
    private int numTitleChanges = 0;
    private String baseTitle;

    // Methods
    public GUI(Client client, String title) {
        super(title);
        this.client = client;
        this.loginScreen = new LoginScreen(this);
        this.lobbyScreen = new LobbyScreen(this);
        this.board = new Board(this, 3);

        add(loginScreen, "Center");
        loginScreen.setVisible(true);

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
        else if (request.startsWith("LOGIN ")) {
            client.handleRequest(request);
        }
        else if (request.startsWith("LOGIN_SUCCESS")) {
            changePanel(loginScreen, lobbyScreen);
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
            String message = "You win!";
            board.setTurnLabel(message);
            JOptionPane.showMessageDialog(this, message);
            changePanel(board, lobbyScreen);
        }
        else if (request.startsWith("DEFEAT")) {
            String message = "You lose!";
            board.setTurnLabel(message);
            JOptionPane.showMessageDialog(this, message);
            changePanel(board, lobbyScreen);
        }
        else if (request.startsWith("TIE")) {
            String message = "You tied!";
            board.setTurnLabel(message);
            JOptionPane.showMessageDialog(this, message);
            changePanel(board, lobbyScreen);
        }
        else if (request.startsWith("MESSAGE")) {
            appendToTitle(request.substring(8));
        }
        else if (request.startsWith("LOBBY")) {
            String[] splitMsg = request.split(" ");
            for (int i=1; i<splitMsg.length; ++i) {
                lobbyScreen.addToWaitList(splitMsg[i]);
            }
        }
        else {
            board.handleRequest(request);
        }
    }

    public void changePanel(JPanel currentPanel, JPanel nextPanel) {
        add(nextPanel);
        nextPanel.setVisible(true);
        currentPanel.setVisible(false);
        remove(currentPanel);
        validate();
        repaint();
    }

    public void toBoard() {
        add(board);
        board.setVisible(true);
        lobbyScreen.setVisible(false);
        remove(lobbyScreen);
        validate();
        repaint();
    }

}
