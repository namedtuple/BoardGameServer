package games;

import server.*;
import shared.Command;
import shared.GameName;
import shared.Request;

public abstract class AbstractGame {

	public enum Turn{
		X,
		O
	}
	protected Turn turn;
	
	protected ServerThread player1;
	protected ServerThread player2;	

	protected Board board;
	protected boolean gameOver;
	
	private Server server;

	public AbstractGame(ServerThread player1, ServerThread player2){
		this.server = player1.getServer();
		this.player1 = player1;
		this.player2 = player2;
		player1.setGame(this);
		player2.setGame(this);

		turn = Turn.X;
		board = new Board(getGameName().getBoardSize());
		gameOver = false;
	}

	public void start(){
        sendNewGameMessage(player1);
        sendNewGameMessage(player2);
	}
	
	public abstract GameName getGameName();

	public abstract boolean legalMove(ServerThread player, Request request);

	public abstract void makeMove(ServerThread player, Request request);

    public void handleRequest(Request request) {
        String[] tokens = request.getTokens();
        Command command = request.getCommand();
        switch(command) {
            case MOVE:
                String username = tokens[1];
                ServerThread player = server.getConnection(username);
                if (legalMove(player, request)) {
                	makeMove(player, request);
                }
                else {
                	System.err.println("?@!?#@!!@#$ Invalid Move");
                }
                break;
        }
    }

	public Turn getOppositeTurn(Turn turn){
		return turn == Turn.X ? Turn.O : Turn.X;
	}

	public ServerThread currentPlayer(){
		return turn == Turn.X ? player1 : player2;
	}
	
	protected void changeTurn(){
		turn = getOppositeTurn(turn);
	}

    public ServerThread otherPlayer(ServerThread player){
    	return player == player1 ? player2 : player1;
    }
    
    
	public boolean gameOver(){
		return gameOver;
	}
    
    protected void endGameWinner(ServerThread winner){
    	gameOver = true;
    	
    	ServerThread loser = otherPlayer(winner);
    	
		winner.send(new Request(Command.VICTORY));
		loser.send(new Request(Command.DEFEAT));
		
		server.addWin(winner.getUserName());
		server.addLoss(loser.getUserName());
    }

    protected void endGameTie(){
    	gameOver = true;
    	player1.send(new Request(Command.TIE));
    	player2.send(new Request(Command.TIE));
    }	
   
	private void sendNewGameMessage(ServerThread player){
		String p1 = player1.getUserName();
		String p2 = player2.getUserName();
		player.send(new Request(Command.NEW_GAME, getGameName() + " " + p1 + " X " + p2 + " O ")); // 'NEW_GAME username1 X username2 O '
	}
}
