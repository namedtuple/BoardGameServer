package client;

import shared.GameName;
import shared.Command;
import shared.Request;

import javax.swing.*;

public class GUI extends JFrame {

    // Fields
    private Client client;
    private LoginScreen loginScreen;
    private LobbyScreen lobbyScreen;
    private AccountCreationScreen accountScreen;
    private BoardScreen boardScreen;
    private JPanel currentScreen;
    private final static String BASE_WINDOW_TITLE = "BoardGameServer";
    private String username;

    // Methods
    public GUI(Client client) {

        super(BASE_WINDOW_TITLE);
        this.client = client;
        this.loginScreen = new LoginScreen(this);
        this.lobbyScreen = new LobbyScreen(this);
        this.accountScreen = new AccountCreationScreen(this);

        add(loginScreen, "Center");
        loginScreen.setVisible(true);
        currentScreen = loginScreen;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(450, 375);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

    }

    private void appendToTitle(String toAppend) {
        setTitle(getTitle() + " - " + toAppend);
    }

    public void handleRequest(Request request) {
        String[] tokens = request.getTokens();
        Command command = request.getCommand();
        switch(command) {
            case MOVE: case JOIN: case LOGGING_IN: case GOTO_LOBBY:
                client.handleRequest(request);
                break;
            case ACCOUNT_CREATION:
                changePanel(accountScreen);
                accountScreen.clearFields();
                break;
            case CREATING_ACCOUNT:
                client.handleRequest(request);
                changePanel(loginScreen);
                break;
            case LOGIN_SUCCESS:
                username = tokens[1];
                appendToTitle(username);
                lobbyScreen.handleRequest(request);
                changePanel(lobbyScreen);
                loginScreen.handleRequest(request);
                break;
            case GET_PROFILE:
                client.handleRequest(request);
                break;
            case PROFILE:
                lobbyScreen.handleRequest(request);
                break;
            case NEW_GAME:
                GameName gameName = GameName.valueOf(tokens[1]);
                boardScreen = new BoardScreen(this, gameName.getBoardSize(), username);
                if (tokens[2].equals(username)) {
                    appendToTitle(tokens[3]);
                } else {
                    appendToTitle(tokens[5]);
                }
                boardScreen.handleRequest(request);
                changePanel(boardScreen);
                break;
            case VICTORY: case DEFEAT: case TIE:
                String message = command.getMessage();
                boardScreen.handleRequest(request);
                JOptionPane.showMessageDialog(this, message);
                lobbyScreen.handleRequest(request);
                changePanel(lobbyScreen);
                setTitle(BASE_WINDOW_TITLE);
                appendToTitle(username);
                break;
            case LOGIN_FAIL:
                JOptionPane.showMessageDialog(this, "Incorrect Username and/or Password");
                break;
            case MOVE_TO: case REMOVE_FROM: case MOVE_BOTH_TO:
                boardScreen.handleRequest(request);
                break;
            case LOBBY:
                lobbyScreen.handleRequest(request);
                break;
            case LOGOUT:
                client.handleRequest(new Request(Command.LOGOUT));  // TODO
                setTitle(BASE_WINDOW_TITLE);
                break;
            case DISCONNECTED:
                changePanel(loginScreen);
                break;
        }
    }

    private void changePanel(JPanel nextPanel) {
        add(nextPanel);
        nextPanel.setVisible(true);
        currentScreen.setVisible(false);
        remove(currentScreen);
        validate();
        repaint();
        currentScreen = nextPanel;
    }

}
