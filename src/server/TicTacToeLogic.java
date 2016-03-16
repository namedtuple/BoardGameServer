package server;

import org.javatuples.Pair;

public class TicTacToeLogic extends AbstractGameLogic {

    // Fields

    // Methods
    public TicTacToeLogic(Server server, ServerThread currentPlayer, ServerThread otherPlayer, int length) {
        super(server, currentPlayer, otherPlayer, length);
    }

    @Override
    public synchronized boolean legalMove(Pair<Integer, Integer> location, ServerThread player) {
        return player == currentPlayer && boardMap.get(location).getOccupant() == null;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public boolean hasWinner() {
        // TL TM TR
        // ML MM MR
        // BL BM BR
        ServerThread TL = boardMap.get(Pair.with(1,1)).getOccupant();
        ServerThread TM = boardMap.get(Pair.with(2,1)).getOccupant();
        ServerThread TR = boardMap.get(Pair.with(3,1)).getOccupant();
        ServerThread ML = boardMap.get(Pair.with(1,2)).getOccupant();
        ServerThread MM = boardMap.get(Pair.with(2,2)).getOccupant();
        ServerThread MR = boardMap.get(Pair.with(3,2)).getOccupant();
        ServerThread BL = boardMap.get(Pair.with(1,3)).getOccupant();
        ServerThread BM = boardMap.get(Pair.with(2,3)).getOccupant();
        ServerThread BR = boardMap.get(Pair.with(3,3)).getOccupant();

        return  (TL != null && TL == TM && TM == TR) ||  // top row
                (ML != null && ML == MM && MM == MR) ||  // middle row
                (BL != null && BL == BM && BM == BR) ||  // bottom row
                (TL != null && TL == ML && ML == BL) ||  // left column
                (TM != null && TM == MM && MM == BM) ||  // middle column
                (TR != null && TR == MR && MR == BR) ||  // right column
                (TL != null && TL == MM && MM == BR) ||  // top-left to bottom-right diagonal
                (TR != null && TR == MM && MM == BL);    // top-right to bottom-left diagonal
    }

    public boolean tied() {
        for (AbstractBoardTile tile : boardMap.values()) {
            if (tile.getOccupant() == null) {
                return false;
            }
        }
        return true;
    }
}
