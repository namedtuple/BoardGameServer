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
        // TL TM TR
        // ML MM MR
        // BL BM BR
        char TL = boardMap.get(Pair.with(1,1));
        char TM = boardMap.get(Pair.with(2,1));
        char TR = boardMap.get(Pair.with(3,1));
        char ML = boardMap.get(Pair.with(1,2));
        char MM = boardMap.get(Pair.with(2,2));
        char MR = boardMap.get(Pair.with(3,2));
        char BL = boardMap.get(Pair.with(1,3));
        char BM = boardMap.get(Pair.with(2,3));
        char BR = boardMap.get(Pair.with(3,3));
        return  (TL != '_' && TL == TM && TM == TR) ||  // top row
                (ML != '_' && ML == MM && MM == MR) ||  // middle row
                (BL != '_' && BL == BM && BM == BR) ||  // bottom row
                (TL != '_' && TL == ML && ML == BL) ||  // left column
                (TM != '_' && TM == MM && MM == BM) ||  // middle column
                (TR != '_' && TR == MR && MR == BR) ||  // right column
                (TL != '_' && TL == MM && MM == BR) ||  // top-left to bottom-right diagonal
                (TR != '_' && TR == MM && MM == BL);    // top-right to bottom-left diagonal
    }

    public boolean boardFilledUp() {
        for (Character c : boardMap.values()) {
            if (c == '_') {
                return false;
            }
        }
        return true;
    }

    public void debugPrintBoard() {
        for (int yRow = 1; yRow <= LENGTH; ++yRow) {
            System.out.print("  ");
            for (int xCol = 1; xCol <= LENGTH; ++xCol) {
                char val = boardMap.get(Pair.with(xCol, yRow));
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

}
