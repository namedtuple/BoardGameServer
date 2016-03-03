import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BoardGameServer {

	public static final short PORT_NUM = 6666;
	
	private ServerSocket serverSocket; 
	
	public BoardGameServer() throws IOException{
		serverSocket = new ServerSocket();
		serverSocket.setReuseAddress(true);
		serverSocket.bind(new InetSocketAddress(PORT_NUM));
	}

	public void run() throws IOException {
		while(true){
			Socket newConnectionSocket = serverSocket.accept();
			System.out.println("New connection from: " + newConnectionSocket.getRemoteSocketAddress());
			
			new BoardGameServerThread(newConnectionSocket).start();
		}
	}
	
	public void close(){
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		BoardGameServer server = null;
		try {
			server = new BoardGameServer();
			server.run();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (server != null){
				server.close();
			}
		}
	}
}
