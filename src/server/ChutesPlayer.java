package server;

import com.sun.corba.se.spi.oa.NullServant;
import com.sun.tools.javac.util.Pair;

/**
 * Created by Jonathan on 3/9/16.
 */
public class ChutesPlayer extends User {
    private int x;
    private int y;
    public ChutesPlayer(String name) {
        super(name);
        //x=0;
        //y=10;
    }
    private Pair<Integer,Integer> currentLocation = null;

    public ChutesPlayer() {
        super();
    }

    public Pair<Integer,Integer> getLocation(){
        return currentLocation;
    }
    public void setLocation(Pair<Integer, Integer> location){ currentLocation = location;}
}
