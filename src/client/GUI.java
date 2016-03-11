package client;
import javax.swing.*;
import java.util.Arrays;
import java.util.HashMap;

public class GUI extends JFrame {

    // Fields
    private Client client;
    private LoginScreen loginScreen;
    private LobbyScreen lobbyScreen;
    private Board board;
    private int numTitleChanges = 0;
    private String baseTitle;
    private HashMap<String, String> requestMessageMap;

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

        requestMessageMap = new HashMap<>();
        requestMessageMap.put("VICTORY", "You win!");
        requestMessageMap.put("DEFEAT", "You lose!");
        requestMessageMap.put("TIE", "You tied!");
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
        else if (request.startsWith("JOIN")) {
            client.handleRequest(request);
        }
        else if (request.startsWith("LOGIN ")) {
            client.handleRequest(request);
        }
        else if (request.startsWith("LOGIN_SUCCESS")) {
            String[] splitMsg = request.split(" ");
            lobbyScreen.setUsername(splitMsg[1]);
            changePanel(loginScreen, Direction.FORWARD);
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
        else if (Arrays.asList(new String[]{"VICTORY", "DEFEAT", "TIE"}).contains(request)) {
            String message = requestMessageMap.get(request);
            board.setTurnLabel(message);
            JOptionPane.showMessageDialog(this, message);
            changePanel(board, Direction.BACKWARD);
        }
        else if (request.startsWith("MESSAGE")) {
            appendToTitle(request.substring(8));
        }
        else if (request.startsWith("LOBBY")) {
            lobbyScreen.addAllToWaitList(request);
        }
        else {
            board.handleRequest(request);
        }
    }

    public void changePanel(JPanel currentPanel, Direction direction) {
        if (currentPanel == loginScreen && direction == Direction.FORWARD) {
            changePanel(currentPanel, lobbyScreen);
        }
        else if (currentPanel == lobbyScreen && direction == Direction.FORWARD) {
            changePanel(currentPanel, board);
        }
        else if (currentPanel == board && direction == Direction.BACKWARD) {
            changePanel(currentPanel, lobbyScreen);
        }
    }

    private void changePanel(JPanel currentPanel, JPanel nextPanel) {
        add(nextPanel);
        nextPanel.setVisible(true);
        currentPanel.setVisible(false);
        remove(currentPanel);
        validate();
        repaint();
    }

}
