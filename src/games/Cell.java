package games;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;


public class Cell {

    // Fields
    private Pair<Integer, Integer> location;
    private List<String> occupants;

    // Methods
    public Cell(Pair<Integer, Integer> location) {
        this.location = location;
        this.occupants = new ArrayList<>();
        this.occupants.add(null);
    }

    public Pair<Integer, Integer> getLocation() {
        return location;
    }

    public void addOccupant(String occupant) {
        occupants.add(occupant);
    }

    public String getOccupant() {
        for (String occupant : occupants) {
            if (occupant != null) {
                return occupant;
            }
        }
        return null;
    }
}
