package games.tic_tac_toe;

import games.AbstractGameLogic;
import games.Cell;
import org.javatuples.Pair;
import server.Server;
import server.ServerThread;

public class TicTacToeLogic extends AbstractGameLogic {

    // Fields

    // Methods
    public TicTacToeLogic(Server server, ServerThread currentPlayer, ServerThread otherPlayer, int length) {
        super(server, currentPlayer, otherPlayer, length);
    }

    @Override
    public synchronized boolean legalMove(Pair<Integer, Integer> location, ServerThread player) {
        return player == currentPlayer && board.getCell(location).getOccupant() == null;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public boolean hasWinner() {
        // TL TM TR
        // ML MM MR
        // BL BM BR

        String TL = board.getCell(Pair.with(1,1)).getOccupant();
        String TM = board.getCell(Pair.with(2,1)).getOccupant();
        String TR = board.getCell(Pair.with(3,1)).getOccupant();
        String ML = board.getCell(Pair.with(1,2)).getOccupant();
        String MM = board.getCell(Pair.with(2,2)).getOccupant();
        String MR = board.getCell(Pair.with(3,2)).getOccupant();
        String BL = board.getCell(Pair.with(1,3)).getOccupant();
        String BM = board.getCell(Pair.with(2,3)).getOccupant();
        String BR = board.getCell(Pair.with(3,3)).getOccupant();

        return  (TL != null && TL.equals(TM) && TM.equals(TR)) ||  // top row
                (ML != null && ML.equals(MM) && MM.equals(MR)) ||  // middle row
                (BL != null && BL.equals(BM) && BM.equals(BR)) ||  // bottom row
                (TL != null && TL.equals(ML) && ML.equals(BL)) ||  // left column
                (TM != null && TM.equals(MM) && MM.equals(BM)) ||  // middle column
                (TR != null && TR.equals(MR) && MR.equals(BR)) ||  // right column
                (TL != null && TL.equals(MM) && MM.equals(BR)) ||  // top-left to bottom-right diagonal
                (TR != null && TR.equals(MM) && MM.equals(BL));    // top-right to bottom-left diagonal
    }

    public boolean tied() {
        for (Cell cell : board.getCells()) {
            if (cell.getOccupant() == null) {
                return false;
            }
        }
        return true;
    }
}
