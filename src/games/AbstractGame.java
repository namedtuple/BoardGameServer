package games;

import server.*;
import shared.Command;
import shared.GameName;
import shared.Request;

public abstract class AbstractGame {
	private Server server;

	protected ServerThread player1;
	protected ServerThread player2;

	protected Board board;
	protected boolean gameOver;

	public AbstractGame(ServerThread player1, ServerThread player2){
		this.server = player1.getServer();
		this.player1 = player1;
		this.player2 = player2;
		player1.setGame(this);
		player2.setGame(this);
		gameOver = false;
		
		board = new Board(getGameName().getBoardSize());
	}

	public void start(){
        sendNewGameMessage(player1);
        sendNewGameMessage(player2);
	}
	
	public abstract GameName getGameName();

	public boolean gameOver(){
		return gameOver;
	}

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

    //different games decide who the current player is
    public abstract ServerThread currentPlayer();

    public ServerThread otherPlayer(ServerThread player){
    	return player == player1 ? player2 : player1;
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
