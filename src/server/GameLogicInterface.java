package server;

import org.javatuples.Pair;

public interface GameLogicInterface {
    boolean legalMove(Pair<Integer, Integer> location, ServerThread player);
    boolean hasWinner();
    boolean tied();
}
