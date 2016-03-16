package server;

import java.util.ArrayList;
import java.util.List;

public class GameLobby {

    // Fields
	private String lobbyName;
    private List<String> list;

    // Methods
	public GameLobby(String lobbyName) {
        this.lobbyName = lobbyName;
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
	public String getLobbyName(){
		return lobbyName;
	}


	//Can be used to send contents of waiting list over socket
    @Override
	public String toString() {
		StringBuilder str = new StringBuilder();
        str.append(lobbyName).append(" ");
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

        player1.send("WELCOME " + p1 + " X " + p2 + " O "); // 'WELCOME username1 X username2 O '
		player1.setOpponent(player2);
		player1.setGame(newGame);

        player2.send("WELCOME " + p2 + " O " + p1 + " X "); // 'WELCOME username2 O username1 X '
		player2.setOpponent(player1);
		player2.setGame(newGame);

        //remove the two players from the lobby
		player1.removeFromLobby();
		player2.removeFromLobby();
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

}
