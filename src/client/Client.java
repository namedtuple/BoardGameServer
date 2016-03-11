package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    // Main
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.run();
    }

    // Fields
    private final static String SERVER_ADDRESS = "localhost";
    private final static int PORT = 6666;
    private BufferedReader in;
    private PrintWriter out;
    private GUI gui;

    private Socket socket;

    // Methods
    public Client() throws IOException {
        Socket socket = new Socket(SERVER_ADDRESS, PORT);
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        gui = new GUI(this, "GUI");
        //LoginScreen loginScreen = new LoginScreen("Login Screen");
    }

    public void run() throws IOException {
        System.out.println("Client running");

        String msg;
        while (true) {
            msg = receive();

            //for testing lobby functionality
            String[] splitMsg = msg.split(" ");
            if (splitMsg[0].equals("LOBBY")){ //"LOBBY user1 user2" <- server sends list of members in the lobby
            	if (splitMsg.length == 1){ //a hack just for testing purposes only
            		System.out.println("LOGIN LENGTH IS 1");
            	}
            	else {
            		System.out.println("Sending JOIN");
            		send("JOIN " + splitMsg[1]); //client sends "JOIN user1" to start a game, forcing them to play
            	}
                handleRequest(msg);
            }


            if (msg.startsWith("WELCOME")) {
                handleRequest(msg);
            }
            else if (msg.startsWith("VALID_MOVE")) {
                handleRequest(msg);
            }
            else if (msg.startsWith("OPPONENT_MOVED")) {
                handleRequest(msg);
            }
            else if (msg.startsWith("VICTORY")) {
                handleRequest(msg);
                break;
            }
            else if (msg.startsWith("DEFEAT")) {
                handleRequest(msg);
                break;
            }
            else if (msg.startsWith("TIE")) {
                handleRequest(msg);
                break;
            }
            else if (msg.startsWith("MESSAGE")) {
                handleRequest(msg);
            }
        }
    }

    // Sends message to Server
    public void send(String message) {
        out.println(message);
    }

    // Returns the next line of stream from Server
    private String receive() throws IOException {
        String msg = in.readLine();
        if (!msg.equals("")) {
            System.out.println("Server says:  " + msg);
        }
        return msg;
    }

    public void handleRequest(String request) {
        if (request.startsWith("MOVE")) {
            send(request);
        }
        else {
            gui.handleRequest(request);
        }
    }

    public boolean attemptLogin(String message){
    	send(message);
        String msg = null;
    	try {
    		msg = receive();
    	} catch (IOException e){
    		e.printStackTrace();
    	}
    	if (msg.equals("LOGIN-SUCCESS")){
    		return true;
    	}
    	else if (msg.equals("LOGIN-FAIL")){
    		return false;
    	}
    	else {
    		//for sanity
    		System.out.println("Unexpected Response for LOGIN");
    		return false;
    	}
    }
}
