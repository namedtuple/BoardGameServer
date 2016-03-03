import org.javatuples.Pair;

public class Main {

    public static void main(String[] args) {
        Board ticTacToeBoard = new Board(3);
        ticTacToeBoard.debugPrintBoardContents();

        ticTacToeBoard.makeMove(2,2, 'X');
        ticTacToeBoard.debugPrintBoardContents();

        Pair<Integer, Integer> loc = Pair.with(1,1);
        ticTacToeBoard.makeMove(loc, 'O');
        ticTacToeBoard.debugPrintBoardContents();

        ticTacToeBoard.debugPrintBoardContentsBetter();
    }
}
