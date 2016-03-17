package games;

import shared.GameName;
import games.checkers.CheckersGame;
import games.tic_tac_toe.TicTacToeGame;
import server.ServerThread;

public class GameFactory {
	
	public static AbstractGame create(GameName gameName, ServerThread p1, ServerThread p2){
		//change to if / else if / else after Chutes is complete
		
		/*if(gameName == GameName.CHUTES_AND_LADDERS){
			return new Chu
		}*/
		if (gameName == GameName.CHECKERS){
			return new CheckersGame(p1, p2);
		}
		if (gameName == GameName.TIC_TAC_TOE){
			return new TicTacToeGame(p1, p2);
		}
		
		return null;
	}
}
