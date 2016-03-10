package server;

import org.javatuples.Pair;

public class TicTacToeLogic extends AbstractGameLogic {

    // Fields

    // Methods
    public TicTacToeLogic(ServerThread currentPlayer, ServerThread otherPlayer, int length) {
        super(currentPlayer, otherPlayer, length);
    }

    @Override
    public synchronized boolean legalMove(Pair<Integer, Integer> location, ServerThread player) {
        if (player == currentPlayer && boardMap.get(location).getOccupant() == null ) {

            // boardmap updated with new player piece
            boardMap.get(location).setOccupant(player);
            currentPlayer = currentPlayer.getOpponent();
            currentPlayer.opponentMoved(location);
            debugPrintBoard();
            return true;
        }
        return false;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public boolean hasWinner() {
        // TL TM TR
        // ML MM MR
        // BL BM BR
        AbstractBoardTile TL = boardMap.get(Pair.with(1,1));
        AbstractBoardTile TM = boardMap.get(Pair.with(2,1));
        AbstractBoardTile TR = boardMap.get(Pair.with(3,1));
        AbstractBoardTile ML = boardMap.get(Pair.with(1,2));
        AbstractBoardTile MM = boardMap.get(Pair.with(2,2));
        AbstractBoardTile MR = boardMap.get(Pair.with(3,2));
        AbstractBoardTile BL = boardMap.get(Pair.with(1,3));
        AbstractBoardTile BM = boardMap.get(Pair.with(2,3));
        AbstractBoardTile BR = boardMap.get(Pair.with(3,3));
        return  (TL.getOccupant() != null && TL == TM && TM == TR) ||  // top row
                (ML.getOccupant() != null && ML == MM && MM == MR) ||  // middle row
                (BL.getOccupant() != null && BL == BM && BM == BR) ||  // bottom row
                (TL.getOccupant() != null && TL == ML && ML == BL) ||  // left column
                (TM.getOccupant() != null && TM == MM && MM == BM) ||  // middle column
                (TR.getOccupant() != null && TR == MR && MR == BR) ||  // right column
                (TL.getOccupant() != null && TL == MM && MM == BR) ||  // top-left to bottom-right diagonal
                (TR.getOccupant() != null && TR == MM && MM == BL);    // top-right to bottom-left diagonal
    }

    public boolean boardFilledUp() {
        for (AbstractBoardTile tile : boardMap.values()) {
            if (tile.getOccupant() == null) {
                return false;
            }
        }
        return true;
    }
}
