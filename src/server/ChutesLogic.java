package server;

import org.javatuples.Pair;

/**
 * Created by Jonathan on 3/9/16.
 */
public class ChutesLogic extends AbstractGameLogic {
    public ChutesLogic(ServerThread currentPlayer, ServerThread otherPlayer, int length) {
        super(currentPlayer, otherPlayer, length);
    }

    public boolean legalMove(Pair<Integer, Integer> location, ServerThread player) {
        return false;
    }

    public boolean hasWinner() {
        char hundred = boardMap.get(Pair.with(1,1));
    }
}
