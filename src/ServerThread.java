import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {

    // Fields
    private BufferedReader in;
    private PrintWriter out;
    private char ID;
    private ServerThread opponentServerThread;
    private Game game;

    // Methods
    public ServerThread(Socket socket, char ID) throws IOException {
        this.ID = ID;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void start(Game game) {
        this.game = game;
        super.start();
    }

    @Override
    public void run() {
        send("WELCOME " + this.ID);
        while (true) {
            try {
                String msg = receive();
                send("You said: " + msg);

                if (msg.startsWith("MOVE")) {
                    if (game.legalMove(extractPosition(msg), this)) {
                        send("VALID_MOVE");
                        send(game.hasWinner() ? "VICTORY" : game.boardFilledUp() ? "TIE" : "");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    // Helper method to obtain position Pair from received String message
    public Pair<Integer, Integer> extractPosition(String message) {
        int i = message.indexOf('[');
        int j = message.indexOf(',');
        int k = message.indexOf(']');
        int x = Integer.parseInt(message.substring(i+1, j).trim());
        int y = Integer.parseInt(message.substring(j+1, k).trim());
        return Pair.with(x, y);
    }

    // Sends message to Client
    private void send(String message) {
        out.println(message);
    }

    // Returns the next line of stream from Client
    private String receive() throws IOException {
        String msg = in.readLine();
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Client " + ID + " says:  " + msg);
        return msg;
    }

    public void opponentMoved(Pair<Integer, Integer> location) {
        send("OPPONENT_MOVED " + location);
        send(game.hasWinner() ? "DEFEAT" : game.boardFilledUp() ? "TIE" : "");
    }

    public char getID() {
        return ID;
    }

    public ServerThread getOpponent() {
        return opponentServerThread;
    }

    public void setOpponent(ServerThread opponentServerThread) {
        this.opponentServerThread = opponentServerThread;
    }

}
