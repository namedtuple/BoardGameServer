import org.javatuples.Pair;

import java.util.HashMap;

public class Game {

    // Fields
    private final static int LENGTH = 3;
    private HashMap<Pair<Integer, Integer>, Character> boardMap;
    private int turn;
    private ServerThread serverThread1;
    private ServerThread serverThread2;
    private ServerThread currentPlayer;

    // Methods
    public Game(ServerThread serverThread1, ServerThread serverThread2) {
        this.serverThread1 = serverThread1;
        this.serverThread2 = serverThread2;
        this.currentPlayer = serverThread1;
        setupBoardMap();
        System.out.println("Game created!");
    }

    private void setupBoardMap() {
        boardMap = new HashMap<>();
        for (int xCol = 1; xCol <= LENGTH; ++xCol) {
            for (int yRow = 1; yRow <= LENGTH; ++yRow) {
                Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
                boardMap.put(pair, '_');
            }
        }
    }

    public synchronized boolean legalMove(Pair<Integer, Integer> location, ServerThread player) {
        if (player == currentPlayer && boardMap.get(location) == '_' ) {
            boardMap.put(location, currentPlayer.getID());
            currentPlayer = currentPlayer.getOpponent();
            currentPlayer.opponentMoved(location);
            return true;
        }
        return false;
    }

    public void makeMove(Pair<Integer, Integer> location) {
        boardMap.put(location, getTurnAsChar());
        switchTurn();
        debugPrintBoardContentsBetter();
    }

    public void switchTurn() {
        turn = turn == 0 ? 1 : 0;
    }

    public char getTurnAsChar() {
        return turn == 0 ? 'X' : 'O';
    }

    public void debugPrintBoardContentsBetter() {
        System.out.print("\n\n");
        System.out.println("GAME CLASS");
        for (int xCol = 1; xCol <= LENGTH; ++xCol) {
            for (int yRow = 1; yRow <= LENGTH; ++yRow) {
                char val = boardMap.get(Pair.with(xCol, yRow));
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    public ServerThread getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean hasWinner() {
        //TODO: implement
        return false;
    }

    public boolean boardFilledUp() {
        //TODO: implement
        return false;
    }
}
