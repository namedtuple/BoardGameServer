import org.javatuples.Pair;

import java.util.HashMap;

public class Game {

    // Fields
    private final static int LENGTH = 3;
    private HashMap<Pair<Integer, Integer>, Character> boardMap;
    private int turn;
    private ServerThread serverThread;

    // Methods
    public Game(ServerThread serverThread) {
        this.serverThread = serverThread;
        setupBoardMap();
        System.out.println("Game created!");
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

    public void makeMove(Pair<Integer, Integer> location, char piece) {
        boardMap.put(location, piece);
    }

    public void switchTurn() {
        turn = turn == 0 ? 1 : 0;
    }

}
