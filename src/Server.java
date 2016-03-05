import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final short PORT_NUM = 6666;

    private ServerSocket serverSocket;

    public Server() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(PORT_NUM));
    }

    public void run() throws IOException {
        while (true) {
            Socket newConnectionSocket1 = serverSocket.accept();
            System.out.println("New connection from: " + newConnectionSocket1.getRemoteSocketAddress());
            ServerThread serverThread1 = new ServerThread(newConnectionSocket1, 'X');
            serverThread1.start();

            Socket newConnectionSocket2 = serverSocket.accept();
            System.out.println("New connection from: " + newConnectionSocket2.getRemoteSocketAddress());
            ServerThread serverThread2 = new ServerThread(newConnectionSocket2, 'O');
            serverThread2.start();
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
}
