package server;

import client.Command;
import client.Request;
import games.GameName;
import games.chutes_and_ladders.ChutesLogic;
import games.tic_tac_toe.TicTacToeLogic;

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
		System.out.println("CURRENT GAME IS" + lobbyName);
		String p1 = player1.getUserName();
		String p2 = player2.getUserName();
		if (lobbyName == GameName.CHUTES_AND_LADDERS) {
			ChutesLogic newGame = new ChutesLogic(server, player1, player2, 10);

			player1.send(new Request(Command.CHUTE));
			player1.setOpponent(player2);
			player1.setGame(newGame);


			player2.send(new Request(Command.CHUTE)); // 'NEW_GAME username2 O username1 X '
			player2.setOpponent(player1);
			player2.setGame(newGame);

		}
        else if (lobbyName == GameName.TIC_TAC_TOE) {
			TicTacToeLogic newGame = new TicTacToeLogic(server, player1, player2, 3);

			player1.send(new Request(Command.NEW_GAME, GameName.TIC_TAC_TOE + " " + p1 + " X " + p2 + " O ")); // 'NEW_GAME username1 X username2 O '
			player1.setOpponent(player2);
			player1.setGame(newGame);

			player2.send(new Request(Command.NEW_GAME, GameName.TIC_TAC_TOE + " " + p2 + " O " + p1 + " X ")); // 'NEW_GAME username2 O username1 X '
			player2.setOpponent(player1);
			player2.setGame(newGame);

		}
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

