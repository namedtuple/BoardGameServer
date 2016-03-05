import org.javatuples.Pair;

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
        Game game = null;
        send("WELCOME");
        while (true) {
            try {
                String msg = receive();
                send("You said: " + msg);
                if (msg.equals("TIC-TAC-TOE")) {
                    game = new Game(this);
                }
                else if (game != null) {
                    if (msg.startsWith("MOVE")) {

                        int i = msg.indexOf('[');
                        int j = msg.indexOf(',');
                        int k = msg.indexOf(']');

                        int x = Integer.parseInt(msg.substring(i+1, j).trim());
                        int y = Integer.parseInt(msg.substring(j+1, k).trim());

                        System.out.println(x);
                        System.out.println(y);

                        game.makeMove(Pair.with(x, y));
                    }
                }
                //send("You said: " + receive());
            } catch (IOException e) {
                e.printStackTrace();
                break;
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
