package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class Client {

    // Main
    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client();
        client.run();
    }

    // Fields
    private final static String SERVER_ADDRESS = "localhost";
    private final static int PORT = 6666;
    private final static int RECONNECT_INTERVAL = 5000;
    private final static int TOTAL_ALLOWED_RECONNECT_PERIOD = 600000; // 10 minutes

    private BufferedReader in;
    private PrintWriter out;
    private GUI gui;

    // Methods
    private Client() throws IOException {
        gui = new GUI(this);
        refreshConnection();
    }
    private void refreshConnection() throws IOException {
        Socket socket = new Socket(SERVER_ADDRESS, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    private void run() throws IOException, InterruptedException {
        System.out.println("Client running");
        Request request;
        int totalDownTime = 0;

        while (true) {
            try {
                request = receive();
                handleRequest(request);
            }
            catch (IOException e) {
                try {
                    handleRequest(new Request("DISCONNECTED"));
                    System.out.println("No connection to server.  Will attempt to reconnect in " + RECONNECT_INTERVAL/1000 + " seconds.");
                    Thread.sleep(RECONNECT_INTERVAL);
                    totalDownTime += RECONNECT_INTERVAL;
                    refreshConnection();
                    System.out.println("  Reconnected!");
                }
                catch (ConnectException e2) {
                    System.out.println("  Failed to reconnect...");
                    System.out.println("");
                    if (totalDownTime > TOTAL_ALLOWED_RECONNECT_PERIOD) {
                       System.out.println("No connection made after " + TOTAL_ALLOWED_RECONNECT_PERIOD/1000 + " seconds.  Exiting program.");
                        break;
                    }
                }
            }
        }
    }

    // Sends message to Server
    private void send(String message) {
        out.println(message);
    }

    // Returns the next line of stream from Server
    private Request receive() throws IOException {
        String msg = in.readLine();
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Server says:  " + msg);
        return new Request(msg);
    }

    public void handleRequest(Request request) {
        Command command = request.getCommand();
        switch (command) {
            case MOVE: case JOIN: case LOGGING_IN: case GOTO_LOBBY: case LOGOUT:
                send(request.getRequest());
                break;
            default:
                if (!request.getRequest().startsWith("You said: ")) {
                    //gui.handleRequest(request.getRequest());
                    gui.handleRequest(request);
                }
                break;
        }
    }

    //public void handleRequest(String request) {
    //    String[] splitRequest = request.split(" ");
    //    String firstToken = splitRequest[0];
    //
    //    if (Arrays.asList("MOVE, JOIN, LOGGING_IN, GOTO_LOBBY, LOGOUT".split(", ")).contains(firstToken)) {
    //        send(request);
    //    }
    //    else if (!request.startsWith("You said: ")) {
    //        gui.handleRequest(request);
    //    }
    //}

}
