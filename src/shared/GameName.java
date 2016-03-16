package shared;

import java.util.ArrayList;
import java.util.List;

public enum GameName {

    // Constants
    TIC_TAC_TOE("Tic-Tac-Toe", 3),
    CHECKERS("Checkers", 8),
    CHUTES_AND_LADDERS("Chutes-and-Ladders", 10);

    // Fields
    private String friendlyName;
    private int boardSize;

    // Methods
    GameName(String friendlyName, int boardSize) {
        this.friendlyName = friendlyName;
        this.boardSize = boardSize;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public static String[] getAllFriendlyNames() {
        List<String> li = new ArrayList<>();
        for (GameName g : GameName.values()) {
             li.add(g.friendlyName);
        }
        String[] arr = new String[li.size()];
        arr = li.toArray(arr);
        return arr;
    }

    public static GameName getGameName(String friendlyName) {
        GameName toReturn = null;
        for (GameName g : GameName.values()) {
            if (g.friendlyName.equals(friendlyName)) {
                toReturn = g;
                break;
            }
        }
        return toReturn;
    }

}
