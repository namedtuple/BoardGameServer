package games.tic_tac_toe;

import games.AbstractGame;
import games.Board;
import games.Cell;
import org.javatuples.Pair;
import server.ServerThread;
import shared.Command;
import shared.GameName;
import shared.Request;

public class TicTacToeGame extends AbstractGame {

    enum Turn{
    	X,
    	O
    }
    
    static final int COLS = 3;
    static final int ROWS = 3;
    
    private Turn turn;

    // Methods
    public TicTacToeGame(ServerThread currentPlayer, ServerThread otherPlayer) {
        super(currentPlayer, otherPlayer);
    	board = new Board(COLS, ROWS);
    	turn = Turn.X;
    }
    
    private boolean validMove(Pair<Integer, Integer> location, ServerThread player) {
        return player == currentPlayer() && board.getCell(location).getOccupant() == null;
    }
    
    private boolean hasWinner() {
        // TL TM TR
        // ML MM MR
        // BL BM BR

        String TL = board.getCell(Pair.with(1,1)).getOccupant();
        String TM = board.getCell(Pair.with(2,1)).getOccupant();
        String TR = board.getCell(Pair.with(3,1)).getOccupant();
        String ML = board.getCell(Pair.with(1,2)).getOccupant();
        String MM = board.getCell(Pair.with(2,2)).getOccupant();
        String MR = board.getCell(Pair.with(3,2)).getOccupant();
        String BL = board.getCell(Pair.with(1,3)).getOccupant();
        String BM = board.getCell(Pair.with(2,3)).getOccupant();
        String BR = board.getCell(Pair.with(3,3)).getOccupant();

        return  (TL != null && TL.equals(TM) && TM.equals(TR)) ||  // top row
                (ML != null && ML.equals(MM) && MM.equals(MR)) ||  // middle row
                (BL != null && BL.equals(BM) && BM.equals(BR)) ||  // bottom row
                (TL != null && TL.equals(ML) && ML.equals(BL)) ||  // left column
                (TM != null && TM.equals(MM) && MM.equals(BM)) ||  // middle column
                (TR != null && TR.equals(MR) && MR.equals(BR)) ||  // right column
                (TL != null && TL.equals(MM) && MM.equals(BR)) ||  // top-left to bottom-right diagonal
                (TR != null && TR.equals(MM) && MM.equals(BL));    // top-right to bottom-left diagonal
    }

    private boolean tied() {
        for (Cell cell : board.getCells()) {
            if (cell.getOccupant() == null) {
                return false;
            }
        }
        return true;
    }

	@Override
	public void start() {
		sendWelcomeMessage();
	}

	@Override
	public boolean legalMove(ServerThread player, Request request) {
		return validMove(extractPosition(request.getRequest()), player);
	}

	@Override
	public void makeMove(ServerThread player, Request request) {
		Pair<Integer, Integer> location = extractPosition(request.getRequest());
		board.getCell(location).addOccupant(player.getUserName());
		
    	otherPlayer(player).send(new Request(Command.OPPONENT_MOVED, request.getRequest().substring(4)));
		if (hasWinner()){
			gameOver = true;
			player.send(new Request(Command.VICTORY));
			otherPlayer(player).send(new Request(Command.DEFEAT));
		}
		else if (tied()){
			gameOver = true;
			player.send(new Request(Command.TIE));
			otherPlayer(player).send(new Request(Command.TIE));
		}
		changeTurn();
	}

	@Override
	public ServerThread currentPlayer() {
		return turn == Turn.X ? player1 : player2;
	}
	
	private void changeTurn(){
		turn = getOppositeTurn(turn);
	}
	
	private Turn getOppositeTurn(Turn turn){
		return turn == Turn.X ? Turn.O : Turn.X;
	}
	
	private void sendWelcomeMessage(){
		String p1 = player1.getUserName();
		String p2 = player2.getUserName();
		player1.send(new Request(Command.NEW_GAME, GameName.TIC_TAC_TOE + " " + p1 + " X " + p2 + " O ")); // 'NEW_GAME username1 X username2 O '
		player2.send(new Request(Command.NEW_GAME, GameName.TIC_TAC_TOE + " " + p2 + " O " + p1 + " X ")); // 'NEW_GAME username2 O username1 X '

	}
	
    private Pair<Integer, Integer> extractPosition(String message) {
        int i = message.indexOf('[');
        int j = message.indexOf(',');
        int k = message.indexOf(']');
        int x = Integer.parseInt(message.substring(i+1, j).trim());
        int y = Integer.parseInt(message.substring(j+1, k).trim());
        return Pair.with(x, y);
    }
}
