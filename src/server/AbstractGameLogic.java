package server;

import org.javatuples.Pair;

import java.util.HashMap;


public abstract class AbstractGameLogic implements GameLogicInterface {

    // Fields
    protected HashMap<Pair<Integer, Integer>, AbstractBoardTile> boardMap;  // change to new 'Board' class later
    protected ServerThread currentPlayer;
    protected ServerThread otherPlayer;
    protected final int LENGTH;

    // Constructor
    public AbstractGameLogic(ServerThread currentPlayer, ServerThread otherPlayer, int length) {
        this.currentPlayer = currentPlayer;
        this.otherPlayer = otherPlayer;
        this.LENGTH = length;
        setupBoardMap();
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
        System.out.println("debugPrintBoard");
        //for (int yRow = 1; yRow <= LENGTH; ++yRow) {
        //    System.out.print("  ");
        //    for (int xCol = 1; xCol <= LENGTH; ++xCol) {
        //        char val = boardMap.get(Pair.with(xCol, yRow));
        //        System.out.print(val + " ");
        //    }
        //    System.out.println();
        //}
    }

}
