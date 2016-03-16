package games;

import shared.Command;
import shared.Request;
import games.tic_tac_toe.TicTacToeBoardTile;
import org.javatuples.Pair;
import server.*;

import java.util.HashMap;

public abstract class AbstractGameLogic implements GameLogicInterface {

    // Fields
    protected HashMap<Pair<Integer, Integer>, AbstractBoardTile> boardMap;  // change to new 'Board' class later
    protected Server server;
    protected ServerThread currentPlayer;
    protected ServerThread otherPlayer;
    protected final int LENGTH;

    // Constructor
    public AbstractGameLogic(Server server, ServerThread player1, ServerThread player2, int length) {
        this.server = server;
        this.currentPlayer = player1;
        this.otherPlayer = player2;
        this.LENGTH = length;
        setupBoardMap();
        System.out.println("Game created: " + player1.getUserName() + " " + player2.getUserName());
    }

    private void setupBoardMap() {
        boardMap = new HashMap<>();
        for (int xCol = 1; xCol <= LENGTH; ++xCol) {
            for (int yRow = 1; yRow <= LENGTH; ++yRow) {
                Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
                boardMap.put(pair, new TicTacToeBoardTile(pair));
            }
        }
    }

    public void debugPrintBoard() {
        //System.out.println("debugPrintBoard");
        //for (int yRow = 1; yRow <= LENGTH; ++yRow) {
        //    System.out.print("  ");
        //    for (int xCol = 1; xCol <= LENGTH; ++xCol) {
        //        char val = boardMap.get(Pair.with(xCol, yRow));
        //        System.out.print(val + " ");
        //    }
        //    System.out.println();
        //}
    }


    // Helper method to obtain position Pair from received String message
    @SuppressWarnings("Duplicates")
    private Pair<Integer, Integer> extractPosition(String message) {
        int i = message.indexOf('[');
        int j = message.indexOf(',');
        int k = message.indexOf(']');
        int x = Integer.parseInt(message.substring(i+1, j).trim());
        int y = Integer.parseInt(message.substring(j+1, k).trim());
        return Pair.with(x, y);
    }

    public void handleRequest(Request request) {
        String[] tokens = request.getTokens();
        Command command = request.getCommand();
        switch(command) {
            case MOVE:
                String username = tokens[1];
                ServerThread player = server.getConnection(username);
                Pair<Integer, Integer> location = extractPosition(request.getRequest());
                if (legalMove(location, player)) {
                    debugPrintBoard();
                    boardMap.get(location).setOccupant(player);
                    currentPlayer = currentPlayer.getOpponent();
                    currentPlayer.opponentMoved(location);
                    player.send(new Request(Command.VALID_MOVE));
                    player.send(hasWinner() ? new Request(Command.VICTORY) : tied() ? new Request(Command.TIE) : new Request(Command.NULL));
                }
                break;
        }
    }

}
