package server;

import shared.Command;
import shared.Request;
import shared.GameName;
import games.AbstractGame;
import games.GameFactory;

import java.util.ArrayList;
import java.util.List;

public class GameLobby {

    // Fields
    private Server server;
    private GameName lobbyName;
    private List<String> list;

    // Methods
	public GameLobby(Server server, GameName lobbyName) {
        this.server = server;
        this.lobbyName = lobbyName;
        list = new ArrayList<>();
	}

	public void addUser(String userName) {
		list.add(userName);
		debugPrintLobbyContents();
	}

	public void removeUser(String userName) {
		list.remove(userName);
		debugPrintLobbyContents();
	}

	public GameName getLobbyName() {
		return lobbyName;
	}


	//Can be used to send contents of waiting list over socket
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(lobbyName).append(" ");
		for (String userName : list) {
			str.append(userName);
			str.append(" ");
		}
		return str.toString();
	}

	public void startGame(ServerThread player1, ServerThread player2) {
        AbstractGame game = GameFactory.create(lobbyName, player1, player2);
        game.start();
	}

    private void debugPrintLobbyContents() {
        System.out.println("-----------------------------------------------------------------------------");
        String lobbyContentsMessage = lobbyName + " lobby contents updated: ";
        if (list.size() == 0) {
            lobbyContentsMessage += "<empty>";
        } else {
            for (String user : list) {
                lobbyContentsMessage += user + " ";
            }
        }
        System.out.println(lobbyContentsMessage);
    }

    public void handleRequest(Request request) { // TODO - Make a copy of Request and Command classes and put in server package.
        String[] tokens = request.getTokens();
        Command command = request.getCommand();
        switch(command) {
            case JOIN:
                ServerThread player1 = server.getConnection(tokens[1]);
                ServerThread player2 = server.getConnection(tokens[2]);
                startGame(player1, player2);
                player1.removeFromLobby();
                player2.removeFromLobby();
                server.sendToAll(new Request(Command.LOBBY, toString()));
                break;
        }
    }
}

