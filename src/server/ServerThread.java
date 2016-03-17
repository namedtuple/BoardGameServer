package server;

import shared.Command;
import shared.Request;
import games.AbstractGame;
import shared.GameName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {

    // Fields
    private BufferedReader in;
    private PrintWriter out;
    private AbstractGame game;
    private String socketAddress;
    private String username;
    private Server server; //reference to the owning server
    private GameLobby lobby; //lobby that the user is currently in
    private static final GameName DEFAULT_LOBBY = GameName.TIC_TAC_TOE;

    // Methods
    public ServerThread(Socket socket, Server server) throws IOException {
        this.server = server;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        lobby = server.getLobby(DEFAULT_LOBBY);
        socketAddress = socket.getRemoteSocketAddress().toString();
    }

    @Override
    public void run() {
        Request request;
        while (true) {
            try {
                request = receive();
                handleRequest(request);
            }
            catch (IOException e) {
                server.debugPrintLostConnectionMessage(username, socketAddress);
                removeFromLobby();
                server.sendToAll(new Request(Command.LOBBY, lobby.toString()));
                server.removeConnection(username);
                break;
            }
        }
    }

    // Sends message to Client
    public void send(Request request) {
        if (!request.getRequest().equals("")) {
            out.println(request.getRequest());
        }
    }

    // Returns the next line of stream from Client
    public Request receive() throws IOException {
        String msg = in.readLine();
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Client " + getUserName() + " says:  " + msg);
        return new Request(msg);
    }

    public Server getServer(){
    	return this.server;
    }
    
    public String getUserName() {
        return username == null ? socketAddress : username;
    }

    public void removeFromLobby(){
        lobby.removeUser(username);
    }

    public void setGame(AbstractGame game){ 
        this.game = game;
    }

    public void handleRequest(Request request) { // TODO - Make a copy of Request and Command classes and put in server package.
        String[] tokens = request.getTokens();
        Command command = request.getCommand();
        switch(command) {
            case LOGGING_IN:
                if (server.login(tokens[1], tokens[2])){
                    username = tokens[1];
                    send(new Request(Command.LOGIN_SUCCESS, username));
                    server.addConnection(username, this);
                }
                else {
                    send(new Request(Command.LOGIN_FAIL));
                }
                break;
            case CREATING_ACCOUNT:            	
                if(!server.createAccount(tokens[1], tokens[2], tokens[3], tokens[4]));
                send(new Request(Command.ACCOUNT_CREATION_FAIL));
                break;
            case GOTO_LOBBY:
                removeFromLobby();
                server.sendToAll(new Request(Command.LOBBY, lobby.toString()));
                lobby = server.getLobby(GameName.valueOf(tokens[1]));
                //lobby = server.getLobby(tokens[1]);
                lobby.addUser(username); //add to new lobby
                server.sendToAll(new Request(Command.LOBBY, lobby.toString()));
                break;
            case JOIN:
                lobby.handleRequest(request);
                break;
            case MOVE:
                game.handleRequest(request);
                break;
            case LOGOUT:
                removeFromLobby();
                server.sendToAll(new Request(Command.LOBBY, lobby.toString()));
                server.removeConnection(username);
                break;
        }
    }
}
