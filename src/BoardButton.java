import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.javatuples.Pair;

public class BoardButton extends JButton{
	private Pair<Integer, Integer> coordinates; //saves button coordinates
	private ImageIcon icon;
	
	public BoardButton(Pair<Integer, Integer> coordinates, ImageIcon icon)
	{
		this.coordinates = coordinates;
		this.icon = icon;
	}
	
	public Pair<Integer, Integer> getCoordinates()
	{
		return coordinates;
	}
	
	
}
