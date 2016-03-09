
public class Main {

    public static void main(String[] args) {
        Board ticTacToeBoard = new Board(3);
        LoginScreen loginScreen = new LoginScreen("Login Screen");
        GUI gui = new GUI(ticTacToeBoard);
    }
}
