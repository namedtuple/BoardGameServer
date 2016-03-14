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
    private HashMap<String, String> requestMessageMap;
    private JPanel currentScreen;
    private final static String BASE_WINDOW_TITLE = "BoardGameServer";
    private String username;

    // Methods
    public GUI(Client client) {
        super(BASE_WINDOW_TITLE);
        this.client = client;
        this.loginScreen = new LoginScreen(this);
        this.lobbyScreen = new LobbyScreen(this);
        //this.board = new Board(this, 3);

        add(loginScreen, "Center");
        loginScreen.setVisible(true);
        currentScreen = loginScreen;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(450, 300);
        setVisible(true);
        setResizable(false);

        requestMessageMap = new HashMap<>();
        requestMessageMap.put("VICTORY", "You win!");
        requestMessageMap.put("DEFEAT", "You lose!");
        requestMessageMap.put("TIE", "You tied!");
        requestMessageMap.put("VALID_MOVE", "It is the opponent's turn");
        requestMessageMap.put("OPPONENT_MOVED", "It is your turn");
    }

    public void appendToTitle(String toAppend) {
        setTitle(getTitle() + " - " + toAppend);
    }

    public void handleRequest(String request) {
        String[] splitRequest = request.split(" ");
        String firstToken = splitRequest[0];

        // UP (Client)
        if (Arrays.asList("MOVE, JOIN, LOGGING_IN, GOTO_LOBBY".split(", ")).contains(firstToken)) {
            client.handleRequest(request);
        }

        // HERE and DOWN (LobbyScreen)
        else if (request.startsWith("LOGIN_SUCCESS")) {
            String[] splitMsg = request.split(" ");
            username = splitMsg[1];
            appendToTitle(username);
            lobbyScreen.setUsername(username);
            lobbyScreen.requestWaitlist();
            changePanel(loginScreen, lobbyScreen);
            loginScreen.clearFields();
        }

        // HERE and DOWN (Board)
        else if (request.startsWith("WELCOME")) {
            board = new Board(this, 3);
            appendToTitle(splitRequest[2]);
            board.setTurnLabel("Player X starts first");
            board.handleRequest(request);
            changePanel(lobbyScreen, board);
        }

        // HERE and DOWN (Board)
        else if (Arrays.asList("VICTORY, DEFEAT, TIE".split(", ")).contains(firstToken)) {
            String message = requestMessageMap.get(request);
            board.setTurnLabel(message);
            board.handleRequest(request);
            JOptionPane.showMessageDialog(this, message);
            lobbyScreen.requestWaitlist();
            changePanel(board, lobbyScreen);
            setTitle(BASE_WINDOW_TITLE);
            appendToTitle(username);
        }

        // DOWN (Board) - VALID_MOVE, OPPONENT_MOVED
        else if (Arrays.asList("VALID_MOVE, OPPONENT_MOVED".split(", ")).contains(firstToken)) {
            board.setTurnLabel(requestMessageMap.get(firstToken));
            board.handleRequest(request);
        }

        // DOWN (LobbyScreen)
        else if (request.startsWith("LOBBY")) {
            lobbyScreen.addAllToWaitList(request);
        }

        else if (request.startsWith("LOGOUT")) {
            client.handleRequest("LOGOUT");
            setTitle(BASE_WINDOW_TITLE);
        }

        else if (request.startsWith("DISCONNECTED")) {
            if (currentScreen == lobbyScreen) {
                changePanel(lobbyScreen, loginScreen);
            }
            else if (currentScreen == board) {
                changePanel(board, loginScreen);
            }
        }

    }

    private void changePanel(JPanel currentPanel, JPanel nextPanel) {
        add(nextPanel);
        nextPanel.setVisible(true);
        currentPanel.setVisible(false);
        remove(currentPanel);
        validate();
        repaint();
        currentScreen = nextPanel;
    }

}
