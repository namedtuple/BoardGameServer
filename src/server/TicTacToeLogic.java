package server;

import org.javatuples.Pair;

public class TicTacToeLogic extends AbstractGameLogic {

    // Fields

    // Methods
    public TicTacToeLogic(ServerThread currentPlayer, ServerThread otherPlayer, int length) {
        super(currentPlayer, otherPlayer, length);
    }

    @Override
    public boolean legalMove(Pair<Integer, Integer> location, ServerThread player) {
        return false;
    }

    @SuppressWarnings("Duplicates")
    @Override
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
}
