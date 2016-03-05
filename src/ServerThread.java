import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket socket;

    private BufferedReader in;
    private PrintWriter out;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out = new PrintWriter(this.socket.getOutputStream(), true);
    }

    public void run() {
        send("Hello new client!");
        while (true) {
            try {
                String msg = receive();
                send("You said: " + msg);
                //send("You said: " + receive());
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
    private String receive() throws IOException {
        String msg = in.readLine();
        System.out.println("Client says:  " + msg);
        return msg;
    }
}
