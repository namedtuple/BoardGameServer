import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BoardGameServerThread extends Thread {

    private Socket socket;

    private BufferedReader in;
    private PrintWriter out;

    public BoardGameServerThread(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out = new PrintWriter(this.socket.getOutputStream(), true);
    }

    public void run() {
        send("Hello new client!");
        while (true) {
            try {
                send("You said: " + recv());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //sends message to client
    private void send(String message) {
        out.println(message);
    }

    //returns the next line of stream
    private String recv() throws IOException {
        return in.readLine();
    }
}
