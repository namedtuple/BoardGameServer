import org.javatuples.Pair;

public class Main {

    public static void main(String[] args) {
        Board ticTacToeBoard = new Board(3);
        GUI gui = new GUI(ticTacToeBoard);
        ticTacToeBoard.debugPrintBoardContentsBetter();

        ticTacToeBoard.makeMove(2,2, 'X');
        ticTacToeBoard.debugPrintBoardContentsBetter();
        gui.update(ticTacToeBoard.getBoardMap());

        Pair<Integer, Integer> loc = Pair.with(1,1);
        ticTacToeBoard.makeMove(loc, 'O');
        ticTacToeBoard.debugPrintBoardContentsBetter();
        gui.update(ticTacToeBoard.getBoardMap());


    }
}
