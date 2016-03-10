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
    
    private char ID;
    private ServerThread opponentServerThread;
    private Game game;
    
    private String username;
    private Server server; //reference to the owning server
    private Socket socket;
    private GameLobby lobby; //lobby that the user is currently in
    

    // Methods
    /*public ServerThread(Socket socket, char ID) throws IOException {
        this.ID = ID;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }*/
    
    public ServerThread(Socket socket, Server server) throws IOException {
        //this.ID = ID;
    	this.socket = socket;
    	this.server = server;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    /*public void start(Game game) {
        this.game = game;
        super.start();
    }*/

    @Override
    public void run() {
        //send("WELCOME " + this.ID + " " + getOpponent().getID());
        //send("WELCOME " + this.ID);
        while (true) {
            try {
                String msg = receive();
                send("You said: " + msg);

                /*if (msg.startsWith("MOVE")) {
                    if (game.legalMove(extractPosition(msg), this)) {
                        send("VALID_MOVE");
                        send(game.hasWinner() ? "VICTORY" : game.boardFilledUp() ? "TIE" : "");
                    }
                }*/
                
                // logging in
                String[] splitMsg = msg.split(" ");
                String firstToken = splitMsg[0];
                if (firstToken.equals("LOGIN")) {
                	//should verify user here
                	username = splitMsg[1];
                	server.addConnection(username, this);
                	lobby = server.getLobby(Server.TIC_TAC_TOE);
                	send("LOBBY " + lobby.toString()); //send contents of lobby before adding them!
                	lobby.addUser(username);
                }
                // possible thread-safety issue, but does not matter for this project
                else if (firstToken.equals("JOIN")) {
                	String otherUser = splitMsg[1];
                	ServerThread otherConnection = server.getConnection(otherUser);
                	lobby.startGame(this, otherConnection);
                }
                //handling moving for game
                else if (firstToken.equals("MOVE")) {
                    if (game.legalMove(extractPosition(msg), this)) {
                        send("VALID_MOVE");
                        send(game.hasWinner() ? "VICTORY" : game.boardFilledUp() ? "TIE" : "");
                    }
                }
                
            } catch (IOException e) {
                e.printStackTrace();
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
        out.println(message);
    }

    // Returns the next line of stream from Client
    public String receive() throws IOException {
        String msg = in.readLine();
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Client " + ID + " says:  " + msg);
        return msg;
    }

    public void opponentMoved(Pair<Integer, Integer> location) {
        send("OPPONENT_MOVED " + location);
        send(game.hasWinner() ? "DEFEAT" : game.boardFilledUp() ? "TIE" : "");
    }

    public void setID(char ID) {
    	this.ID = ID;
    }
    
    public char getID() {
        return ID;
    }

    public ServerThread getOpponent() {
        return opponentServerThread;
    }

    public void setOpponent(ServerThread opponentServerThread) {
        this.opponentServerThread = opponentServerThread;
    }
    
    public String getUserName(){
    	return this.username;
    }
    
    public void removeFromLobby(){
    	lobby.removeUser(username);
    	lobby = null;
    }
    
    public void setGame(Game game){
    	this.game = game;
    }

}
