import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.javatuples.Pair;

public class Tile extends JButton {
    private Pair<Integer, Integer> coordinates; //saves button coordinates
    private ImageIcon icon;

    public Tile(Pair<Integer, Integer> coordinates, ImageIcon icon) {
        this.coordinates = coordinates;
        this.icon = icon;
    }

    public Pair<Integer, Integer> getCoordinates() {
        return coordinates;
    }
}
