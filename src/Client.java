import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    // Fields
    private final static String SERVER_ADDRESS = "localhost";
    private final static int PORT = 6666;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    // Methods
    public static void main(String[] args) throws IOException {
        Board ticTacToeBoard = new Board(3);
        Client client = new Client();
        GUI gui = new GUI(ticTacToeBoard, client);
    }

    public Client() throws IOException {
        socket = new Socket(SERVER_ADDRESS, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        receive();
        send("Applesauce is tasty.");
    }

    private void send(String message) {
        out.println(message);
    }

    private String receive() throws IOException {
        String msg = in.readLine();
        System.out.println("Server says:  " + msg);
        return msg;
    }

}
