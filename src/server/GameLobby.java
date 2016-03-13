package server;

import java.util.ArrayList;
import java.util.List;

public class GameLobby {

    // Fields
	private List<String> list;

    // Methods
	public GameLobby(){
		list = new ArrayList<>();
	}

	public void addUser(String userName){
		list.add(userName);
        debugPrintLobbyContents();
	}

	public void removeUser(String userName){
		list.remove(userName);
        debugPrintLobbyContents();
	}

	//Can be used to send contents of waiting list over socket
    @Override
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

        String p1 = player1.getUserName();
        String p2 = player2.getUserName();

        player1.send("WELCOME " + p1 + " X " + p2 + " O "); // WELCOME username1=X username2=O
		player1.setOpponent(player2);
		player1.setGame(newGame);

        player2.send("WELCOME " + p2 + " O " + p1 + " X "); // WELCOME username1=X username2=O
		player2.setOpponent(player1);
		player2.setGame(newGame);

        //remove the two players from the lobby
		player1.removeFromLobby();
		player2.removeFromLobby();
	}

    private void debugPrintLobbyContents() {
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Lobby contents updated: ");
        for (String user : list) {
            System.out.println("  " + user);
        }
    }

}
