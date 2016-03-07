import org.javatuples.Pair;

import java.util.HashMap;

public class Game {

    // Fields
    private final static int LENGTH = 3;
    private HashMap<Pair<Integer, Integer>, Character> boardMap;
    private ServerThread currentPlayer;

    // Methods
    public Game(ServerThread currentPlayer) {
        this.currentPlayer = currentPlayer;
        setupBoardMap();
        System.out.println("Game created");
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
            debugPrintBoard();
            return true;
        }
        return false;
    }

    public boolean hasWinner() {
        //TODO: implement
        return false;
    }

    public boolean boardFilledUp() {
        //TODO: implement
        return false;
    }

    public void debugPrintBoard() {
        for (int xCol = 1; xCol <= LENGTH; ++xCol) {
            System.out.print("  ");
            for (int yRow = 1; yRow <= LENGTH; ++yRow) {
                char val = boardMap.get(Pair.with(xCol, yRow));
                System.out.print(val + " ");
            }
            System.out.println();
        }
        System.out.print("\n\n");
    }

}
