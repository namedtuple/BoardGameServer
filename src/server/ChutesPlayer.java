package server;

import com.sun.corba.se.spi.oa.NullServant;

/**
 * Created by Jonathan on 3/9/16.
 */
public class ChutesPlayer extends User {
    public ChutesPlayer(String name) {
        super(name);
    }
    private Pair<Integer,Integer> currentLocation = null;
    public Pair<Integer,Integer> getlocation(){
        return currentLocation;
    }

}
