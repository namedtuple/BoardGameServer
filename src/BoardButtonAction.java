import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class BoardButtonAction implements ActionListener
{

	private Board board;

	public BoardButtonAction(Board board)
	{
		this.board = board;
	}

	@Override
	/*
	 * This will execute when a button object is clicked on the game board.
	 * Gameboard will be properly updated to what is clicked as well.
	 */
	public void actionPerformed(ActionEvent e)
	{
		BoardButton command = (BoardButton) e.getSource();
		char iconChar = board.getTurn() == 0 ? 'X' : 'O';
		command.setIcon(chooseIcon(iconChar));
		command.setDisabledIcon(chooseIcon(iconChar));
		command.setEnabled(false);
		board.makeMove(command.getCoordinates(), iconChar);
		board.debugPrintBoardContents();
		board.debugPrintBoardContentsBetter();
		board.switchTurn();
	}

    //helper method that will return an X or O icon
    public ImageIcon chooseIcon(char piece)
    {
        ImageIcon icon = new ImageIcon(
                new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB));
    	if(piece == 'X')
    	{
    		//icon = new ImageIcon(getClass().getResource("/../img/X.png"));
            try {
                icon = new ImageIcon(ImageIO.read(new File("img/x.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    	else if(piece == 'O')
    	{
    		//icon = new ImageIcon(getClass().getResource("/../img/O.png"));
            try {
                icon = new ImageIcon(ImageIO.read(new File("img/o.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    	return icon;
    }
}
