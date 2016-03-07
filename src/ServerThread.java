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
    private char ID;
    private ServerThread opponentServerThread;
    private Game game;

    public ServerThread(Socket socket, char ID) throws IOException {
        this.socket = socket;
        this.ID = ID;

        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out = new PrintWriter(this.socket.getOutputStream(), true);
    }

    public void start(Game game) {
        this.game = game;
        super.start();
    }

    public void run() {
        send("WELCOME " + this.ID);
        while (true) {
            try {
                String msg = receive();
                send("You said: " + msg);
                if (msg.startsWith("MOVE")) {

                    int i = msg.indexOf('[');
                    int j = msg.indexOf(',');
                    int k = msg.indexOf(']');

                    int x = Integer.parseInt(msg.substring(i+1, j).trim());
                    int y = Integer.parseInt(msg.substring(j+1, k).trim());

                    System.out.println(x);
                    System.out.println(y);

                    if (game.legalMove(Pair.with(x, y), this)) {
                        send("VALID_MOVE");
                        send(game.hasWinner() ? "VICTORY" : game.boardFilledUp() ? "TIE" : "");
                    }
                    //game.makeMove(Pair.with(x, y));
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

    public ServerThread getOpponent() {
        return opponentServerThread;
    }
    public void setOpponent(ServerThread opponentServerThread) {
        this.opponentServerThread = opponentServerThread;
    }

    public char getID() {
        return ID;
    }

    public void opponentMoved(Pair<Integer, Integer> location) {
        send("OPPONENT_MOVED " + location);
        send(game.hasWinner() ? "DEFEAT" : game.boardFilledUp() ? "TIE" : "");
    }
}
