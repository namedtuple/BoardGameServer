package games;

import org.javatuples.Pair;
import server.ServerThread;

public interface GameLogicInterface {
    boolean legalMove(Pair<Integer, Integer> location, ServerThread player);
    //boolean legalMove(ServerThread player);
    boolean hasWinner();
    boolean tied();
}
