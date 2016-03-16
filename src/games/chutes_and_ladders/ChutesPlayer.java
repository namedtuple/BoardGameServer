package games.chutes_and_ladders;
import org.javatuples.Pair;
import server.User;

/**
 * Created by Jonathan on 3/9/16.
 */
public class ChutesPlayer extends User {
    private int x;
    private int y;
    private Pair<Integer, Integer> currentLocation = null;

    public ChutesPlayer() {
        super();
    }

    public ChutesPlayer(String name) {
        super(name);
        //x=0;
        //y=10;
    }


    public Pair<Integer, Integer> getLocation() {
        return currentLocation;
    }

    public void setLocation(Pair<Integer, Integer> location) {
        currentLocation = location;
    }

}
