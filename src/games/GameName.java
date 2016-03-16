package games;

import java.util.ArrayList;
import java.util.List;

public enum GameName {

    // Constants
    TIC_TAC_TOE("Tic-Tac-Toe"),
    CHECKERS("Checkers"),
    CHUTES_AND_LADDERS("Chutes-and-Ladders");

    // Fields
    private String friendlyName;

    // Methods
    GameName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
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
