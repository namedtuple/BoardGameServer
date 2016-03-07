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

    // Methods
    public Client() throws IOException {
        Socket socket = new Socket(SERVER_ADDRESS, PORT);
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
}
