import org.javatuples.Pair;

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
                char ID = msg.charAt(8);
                getSlave().getSlave().setID(ID);
                getSlave().setTitle("" + ID);
            }
            else if (msg.startsWith("VALID_MOVE")) {
                // Set position to appropriate icon.
                // How do we know which icon to set?
                //   Because this is VALID_MOVE and not OPPONENT_MOVED; so, use ID of Board.
                getSlave().getSlave().getLastClickedTile().setIcon();
                getSlave().appendToTitle("Opponent's turn");
            }
            else if (msg.startsWith("OPPONENT_MOVED")) {
                getSlave().getSlave().getSlave(extractPosition(msg)).setOpponentIcon();
                getSlave().appendToTitle("Your turn");
            }
            else if (msg.startsWith("VICTORY")) {
                getSlave().appendToTitle("You win");
                break;
            }
            else if (msg.startsWith("DEFEAT")) {
                getSlave().appendToTitle("You lose");
                break;
            }
            else if (msg.startsWith("TIE")) {
                getSlave().appendToTitle("You tied");
                break;
            }
            else if (msg.startsWith("MESSAGE")) {
                getSlave().appendToTitle(msg.substring(8));
            }
        }
    }

    // Helper method to obtain position Pair from received String message
    private Pair<Integer, Integer> extractPosition(String message) {
        int i = message.indexOf('[');
        int j = message.indexOf(',');
        int k = message.indexOf(']');
        int x = Integer.parseInt(message.substring(i+1, j).trim());
        int y = Integer.parseInt(message.substring(j+1, k).trim());
        return Pair.with(x, y);
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

    public GUI getSlave() {
        return gui;
    }

}
