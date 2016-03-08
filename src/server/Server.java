package server;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    // Main
    public static void main(String[] args) {
        Server server = null;
        try {
            server = new Server();
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                server.close();
            }
        }
    }

    // Fields
    public static final short PORT_NUM = 6666;
    private ServerSocket serverSocket;

    // Methods
    public Server() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(PORT_NUM));
    }

    public void run() throws IOException {
        System.out.println("Server running");
        while (true) {

            Socket newConnectionSocket1 = serverSocket.accept();
            System.out.println("New connection from: " + newConnectionSocket1.getRemoteSocketAddress());
            ServerThread serverThread1 = new ServerThread(newConnectionSocket1, 'X');

            Socket newConnectionSocket2 = serverSocket.accept();
            System.out.println("New connection from: " + newConnectionSocket2.getRemoteSocketAddress());
            ServerThread serverThread2 = new ServerThread(newConnectionSocket2, 'O');

            serverThread1.setOpponent(serverThread2);
            serverThread2.setOpponent(serverThread1);

            Game game = new Game(serverThread1);

            serverThread1.start(game);
            serverThread2.start(game);
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
