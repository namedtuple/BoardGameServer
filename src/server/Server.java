package server;

import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    // Main
    public static void main(String[] args) {
        Server server = null;
        try {
            server = new Server();
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                server.close();
            }
        }
    }

    // Fields
    public static final short PORT_NUM = 6666;
    public static final String TIC_TAC_TOE = "Tic-Tac-Toe";
    public static final String CHECKERS = "Checkers";
    public static final String CHUTES_AND_LADDERS = "Chutes and Ladders";
    private ServerSocket serverSocket;
    private AccountAuthenticator accountAuthenticator;

    private Map<String, GameLobby> gameLobbies; //stores a Lobby for each game, where String is name of game
    private Map<String, ServerThread> connectionHandlers; //stores connection handler for each user

    // Methods
    public Server() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(PORT_NUM));

        accountAuthenticator = new AccountAuthenticator();

        gameLobbies = new HashMap<String, GameLobby>();
        gameLobbies.put(TIC_TAC_TOE, new GameLobby());
        gameLobbies.put(CHECKERS, new GameLobby());
        gameLobbies.put(CHUTES_AND_LADDERS, new GameLobby());

        connectionHandlers = new HashMap<String, ServerThread>();

    }

    public void run() throws IOException {
        System.out.println("Server running");
        while (true) {
        	Socket newConnectionSocket = serverSocket.accept();
            System.out.println("New connection from: " + newConnectionSocket.getRemoteSocketAddress());
            ServerThread serverThread = new ServerThread(newConnectionSocket, this);
            serverThread.start();
        }
    }

    public GameLobby getLobby(String lobbyName){
    	return gameLobbies.get(lobbyName);
    }

    //called when a user successfully logs in
    public void addConnection(String userName, ServerThread connection){
    	connectionHandlers.put(userName, connection);
    }

    public ServerThread getConnection(String userName){
    	return connectionHandlers.get(userName);
    }

    public boolean login(String enteredName, String enteredPassword){
    	if (accountAuthenticator.userExists(enteredName)){ //if user exists
    		return accountAuthenticator.isValidLogin(enteredName, enteredPassword); //check password
    	}
    	else { //create account, should return true and login the user
    		return accountAuthenticator.addUser(enteredName, enteredPassword);
    	}
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToAll(String message) {
        for (ServerThread st : connectionHandlers.values()) {
            st.send(message);
        }
    }

}
