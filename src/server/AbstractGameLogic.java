
// This is a comment
public abstract class AbstractGameLogic {
	public ServerThread currentPlayer;
	public ServerThread opponent;
	public Board board;
	public abstract boolean makeMove();
	public abstract boolean hasWinner();
	public abstract boolean legalMove();
}
