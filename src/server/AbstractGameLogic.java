package server;

import org.javatuples.Pair;

import java.util.HashMap;


public abstract class AbstractGameLogic implements GameLogicInterface {

    // Fields
    protected HashMap<Pair<Integer, Integer>, AbstractBoardTile> boardMap;  // change to new 'Board' class later
    private ServerThread currentPlayer;
    private ServerThread otherPlayer;
    private final int LENGTH;

    // Constructor
    public AbstractGameLogic(ServerThread currentPlayer, ServerThread otherPlayer, int length) {
        this.currentPlayer = currentPlayer;
        this.otherPlayer = otherPlayer;
        this.LENGTH = length;
    }

    private void setupBoardMap() {
        boardMap = new HashMap<>();
        for (int xCol = 1; xCol <= LENGTH; ++xCol) {
            for (int yRow = 1; yRow <= LENGTH; ++yRow) {
                Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
                boardMap.put(pair, new TicTacToeBoardTile());
            }
        }
    }

}
