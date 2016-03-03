import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;


public class Board {

    // Fields
    private HashMap<Pair, String> boardMap;

    // Methods
    public Board(int size) {
        boardMap = new HashMap<>();
        for (int row=1; row<=size; ++row) {
            for (int col=1; col<=size; ++col) {
                Pair<Integer, Integer> pair = Pair.with(row,col);
                boardMap.put(pair, "_");
            }
        }
    }

    public void debugPrintBoardContents() {
        for (Map.Entry<Pair, String> entry : boardMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
