package server;

import java.util.ArrayList;
import java.util.List;

public class GameLobby {

	private List<String> list;

	public GameLobby(){
		list = new ArrayList<String>();
	}

	public void addUser(String userName){
		list.add(userName);
	}

	public void removeUser(String userName){
		list.remove(userName);
	}

	//Can be used to send contents of waiting list over socket
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(String userName : list){
			str.append(userName);
			str.append(" ");
		}
		return str.toString();
	}

	public void startGame(ServerThread player1, ServerThread player2){

		//create game, giving it necessary parameters
		TicTacToeLogic newGame = new TicTacToeLogic(player1, player2, 3);
		player1.send("WELCOME " + "X O");
		player1.setID('X');
		player1.setOpponent(player2);
		player1.setGame(newGame);

		player2.send("WELCOME " + "O X");
		player2.setID('O');
		player2.setOpponent(player1);
		player2.setGame(newGame);
		//remove the two players from the lobby
		player1.removeFromLobby();
		player2.removeFromLobby();
		//start the game


	}
}
