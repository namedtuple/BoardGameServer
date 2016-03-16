package client;

import javax.swing.*;

public class GUI extends JFrame {

    // Fields
    private Client client;
    private LoginScreen loginScreen;
    private LobbyScreen lobbyScreen;
    private AccountCreationScreen accountScreen;
    private Board board;
    private JPanel currentScreen;
    private final static String BASE_WINDOW_TITLE = "BoardGameServer";
    private String username;

    // Methods
    public GUI(Client client) {
        super(BASE_WINDOW_TITLE);
        this.client = client;
        this.loginScreen = new LoginScreen(this);
        this.lobbyScreen = new LobbyScreen(this);
<<<<<<< HEAD
=======
        this.accountScreen = new AccountCreationScreen(this);
        //this.board = new Board(this, 3);
>>>>>>> 7b5f28df0a08f167af39f4e9de61b31af276e38a

        add(loginScreen, "Center");
        loginScreen.setVisible(true);
        currentScreen = loginScreen;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(450, 300);
        setVisible(true);
        setResizable(false);

    }

    private void appendToTitle(String toAppend) {
        setTitle(getTitle() + " - " + toAppend);
    }

<<<<<<< HEAD
    public void handleRequest(Request request) {
        String[] tokens = request.getTokens();
        Command command = request.getCommand();
        switch(command) {
            case MOVE: case JOIN: case LOGGING_IN: case GOTO_LOBBY:
                client.handleRequest(request);
                break;
            case LOGIN_SUCCESS:
                username = tokens[1];
                appendToTitle(username);
                lobbyScreen.handleRequest(request);
                changePanel(lobbyScreen);
                loginScreen.handleRequest(request);
                break;
            case WELCOME:
                board = new Board(this, 3);
                appendToTitle(tokens[2]);
                board.handleRequest(request);
                changePanel(board);
                break;
            case VICTORY: case DEFEAT: case TIE:
                String message = command.getMessage();
                board.handleRequest(request);
                JOptionPane.showMessageDialog(this, message);
                lobbyScreen.handleRequest(request);
                changePanel(lobbyScreen);
                setTitle(BASE_WINDOW_TITLE);
                appendToTitle(username);
                break;
            case VALID_MOVE: case OPPONENT_MOVED:
                board.handleRequest(request);
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
            default:
                break;
=======
    public void handleRequest(String request) {
        String[] splitRequest = request.split(" ");
        String firstToken = splitRequest[0];

        // UP (Client)
        if (Arrays.asList("MOVE, JOIN, LOGGING_IN, GOTO_LOBBY".split(", ")).contains(firstToken)) {
            client.handleRequest(request);
        }
        
        // HERE and DOWN (AccountCreationScreen)
        else if (request.startsWith("ACCOUNT_CREATION")) {
            appendToTitle("Create New Account");
            changePanel(loginScreen, accountScreen);
            accountScreen.clearFields();
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
>>>>>>> 7b5f28df0a08f167af39f4e9de61b31af276e38a
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
