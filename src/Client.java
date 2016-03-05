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
        Client client = new Client();
        GUI gui = new GUI(client);
        client.go();
    }

    public Client() throws IOException {
        socket = new Socket(SERVER_ADDRESS, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void go() throws IOException {
        while (true) {
            if (receive().equals("WELCOME")) {
                send("TIC-TAC-TOE");
                // create Board here, as opposed to in GUI constructor?
            }
        }
    }

    public void send(String message) {
        out.println(message);
    }

    private String receive() throws IOException {
        String msg = in.readLine();
        System.out.println("Server says:  " + msg);
        return msg;
    }

}