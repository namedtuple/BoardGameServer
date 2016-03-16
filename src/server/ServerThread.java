package server;

import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {

    // Fields
    private BufferedReader in;
    private PrintWriter out;
    private ServerThread opponentServerThread;
    private AbstractGameLogic game;
    private String socketAddress;
    private String username;
    private Server server; //reference to the owning server
    private GameLobby lobby; //lobby that the user is currently in
    private static final String defaultLobby = "Tic-Tac-Toe";

    // Methods
    public ServerThread(Socket socket, Server server) throws IOException {
    	this.server = server;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        lobby = server.getLobby(defaultLobby);
        socketAddress = socket.getRemoteSocketAddress().toString();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String msg = receive();
                send("You said: " + msg);

                String[] splitMsg = msg.split(" ");
                String firstToken = splitMsg[0];
                if (firstToken.equals("LOGGING_IN")) {
                	if (server.login(splitMsg[1], splitMsg[2])){
	                	username = splitMsg[1];
                		send("LOGIN_SUCCESS " + username);
	                	server.addConnection(username, this);
                	}
                	else {
                		send("LOGIN_FAIL");
                	}
                }

                // received when client changes lobby from the dropdown menu
                else if (firstToken.equals("GOTO_LOBBY")){
                    removeFromLobby(); //remove from current lobby
                    server.sendToAll("LOBBY " + lobby.toString());
                    lobby = server.getLobby(splitMsg[1]);
                    lobby.addUser(username); //add to new lobby
                    server.sendToAll("LOBBY " + lobby.toString());
                }

                // possible thread-safety issue, but does not matter for this project
                else if (firstToken.equals("JOIN")) {
                	String otherUser = splitMsg[1];
                	ServerThread otherConnection = server.getConnection(otherUser);
                	lobby.startGame(this, otherConnection);
                    server.sendToAll("LOBBY " + lobby.toString());
                }
                //handling moving for game
                else if (firstToken.equals("MOVE")) {
                    //if (lobby.getLobbyName().equalsIgnoreCase("Chutes-N-Ladders")){
                    //    if (game.legalMove(this)){
                    //        send("VALID_MOVE");
                    //        send(game.hasWinner() ? "VICTORY" : game.tied() ? "TIE" : "");
                    //    }
                    //}
                    if (lobby.getLobbyName().equalsIgnoreCase("Tic-Tac-Toe")){
                        if (game.legalMove(extractPosition(msg), this)) {
                            send("VALID_MOVE");
                            send(game.hasWinner() ? "VICTORY" : game.tied() ? "TIE" : "");
                        }
                    }
                    //else if (lobby.getLobbyName().equalsIgnoreCase("Checkers")){
                    //    if (game.legalMove(extractPosition(msg), this)) {
                    //        send("VALID_MOVE");
                    //        send(game.hasWinner() ? "VICTORY" : game.tied() ? "TIE" : "");
                    //    }
                    //}



                }

                else if (firstToken.equals("LOGOUT")) {
                    removeFromLobby();
                    server.sendToAll("LOBBY " + lobby.toString());
                    server.removeConnection(username);
                }

            }
            catch (IOException e) {
                server.debugPrintLostConnectionMessage(username, socketAddress);
                removeFromLobby();
                server.sendToAll("LOBBY " + lobby.toString());
                server.removeConnection(username);
                break;
            }
        }
    }

    // Helper method to obtain position Pair from received String message
    @SuppressWarnings("Duplicates")
    public Pair<Integer, Integer> extractPosition(String message) {
        int i = message.indexOf('[');
        int j = message.indexOf(',');
        int k = message.indexOf(']');
        int x = Integer.parseInt(message.substring(i+1, j).trim());
        int y = Integer.parseInt(message.substring(j+1, k).trim());
        return Pair.with(x, y);
    }

    // Sends message to Client
    public void send(String message) {
        if (!message.equals("")) {
            out.println(message);
        }
    }

    // Returns the next line of stream from Client
    public String receive() throws IOException {
        String msg = in.readLine();
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Client " + getUserName() + " says:  " + msg);
        return msg;
    }

    public void opponentMoved(Pair<Integer, Integer> location) {
        send("OPPONENT_MOVED " + location);
        send(game.hasWinner() ? "DEFEAT" : game.tied() ? "TIE" : "");
    }

    public ServerThread getOpponent() {
        return opponentServerThread;
    }

    public void setOpponent(ServerThread opponentServerThread) {
        this.opponentServerThread = opponentServerThread;
    }

    public String getUserName() {
        return username == null ? socketAddress : username;
    }

    public void removeFromLobby(){
        lobby.removeUser(username);
    }

    public void setGame(AbstractGameLogic game){
    	this.game = game;
    }

}
