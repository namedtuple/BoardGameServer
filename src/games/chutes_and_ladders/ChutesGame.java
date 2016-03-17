package games.chutes_and_ladders;

import games.AbstractGame ;
import games.Board;
import org.javatuples.Pair;
import server.ServerThread;
import shared.Command;
import shared.GameName;
import shared.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class ChutesGame extends AbstractGame {

    public static final int COLS = 10;
    public static final int ROWS = 10;

    public static final int NUM_CELLS = COLS * ROWS;

    private ServerThread currentPlayer;
    private Map<ServerThread, Integer> playerLocations;
    private Map<Integer, Integer> destinationMap;

    private Random random;

    // Methods
    public ChutesGame(ServerThread player1, ServerThread player2) {
        super(player1, player2);

        currentPlayer = player1;
        board = new Board(COLS, ROWS);
        random = new Random();

        playerLocations = new HashMap<ServerThread, Integer>();
        playerLocations.put(player1, 0);
        playerLocations.put(player2, 0);

        destinationMap = new HashMap<Integer,Integer>();
        for (int i = 1; i <= NUM_CELLS; ++i){
        	destinationMap.put(i, i);
        }

        //chutes
        destinationMap.put(16, 6);
        destinationMap.put(47, 26);
        destinationMap.put(49, 11);
        destinationMap.put(56, 53);
		destinationMap.put(62, 19);
		destinationMap.put(64, 60);
		destinationMap.put(87, 24);
		destinationMap.put(93, 73);
		destinationMap.put(95, 75);
		destinationMap.put(98, 78);

        //ladders
		destinationMap.put(1, 38);
		destinationMap.put(4, 14);
		destinationMap.put(9, 31);
		destinationMap.put(21, 42);
		destinationMap.put(28, 84);
		destinationMap.put(36, 44);
		destinationMap.put(51, 67);
		destinationMap.put(71, 91);
		destinationMap.put(80, 100);

    }

    @Override
    public void start() {
		String p1 = player1.getUserName();
		String p2 = player2.getUserName();
        player1.send(new Request(Command.NEW_GAME, GameName.CHUTES_AND_LADDERS + " " + p1 + " X " + p2 + " O ")); // 'NEW_GAME username1 X username2 O '
		player2.send(new Request(Command.NEW_GAME, GameName.CHUTES_AND_LADDERS + " " + p1 + " X " + p2 + " O ")); // 'NEW_GAME username2 O username1 X '
    }

    @Override
    public boolean legalMove(ServerThread player, Request request) {
    	return player == currentPlayer; //just "roll"
    }

    @Override
    public void makeMove(ServerThread player, Request request) {
    	int movement = rollDice();
    	int oldPosition = playerLocations.get(player);
    	int newPosition = oldPosition + movement;
    	int newActualPosition = destinationMap.get(newPosition);
    	playerLocations.put(player, newActualPosition);

    	if (newActualPosition > 100) { //overshot the end, don't move
    		newActualPosition = oldPosition;
    	}

    	Pair<Integer, Integer> newPosAsPair = locationToCoord(newActualPosition);
    	player.send(new Request(Command.MOVE_TO, player.getUserName() + " " + newPosAsPair.toString()));
    	otherPlayer(player).send(new Request(Command.MOVE_TO, player.getUserName() + " " + newPosAsPair.toString()));

    	if (newActualPosition == 100){
    		gameOver = true;
    		player.send(new Request(Command.VICTORY));
    		otherPlayer(player).send(new Request(Command.DEFEAT));
    	}

    	changeTurn();
    }

    @Override
    public ServerThread currentPlayer() {
        return currentPlayer;
    }

    private void changeTurn(){
    	currentPlayer = otherPlayer(currentPlayer);
    }

    private Pair<Integer, Integer> locationToCoord(int location){
    	//math to convert 1 - 100 to col, row for Chutes
    	int adj = location - 1;
    	int row = 10 - (adj / 10);

    	if (row % 2 == 0){
    		int col = location % 10;
    		if (col == 0){
    			col = 10;
    		}
    		return Pair.with(col, row);
    	}
    	else {
    		return Pair.with(10 - (adj % 10), row);
    	}
    }

    private int rollDice(){
    	return random.nextInt(6) + 1;
    }
}

