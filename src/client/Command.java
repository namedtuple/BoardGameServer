package client;

public enum Command {

    // Constants
    LOGGING_IN, LOGIN_SUCCESS, LOGIN_FAIL,
    LOGOUT, DISCONNECTED,
    GOTO_LOBBY, LOBBY,
    JOIN, WELCOME,
    MOVE, VALID_MOVE("It is your opponent's turn"), OPPONENT_MOVED("It is your turn"),
    VICTORY("You win!"), DEFEAT("You lose!"), TIE("You tied!"),
    NULL;

    // Fields
    private String message;

    // Methods
    Command () {
    }

    Command (String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
