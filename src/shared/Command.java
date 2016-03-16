package shared;

public enum Command {

    // Constants
    LOGGING_IN, LOGIN_SUCCESS, LOGIN_FAIL,
    ACCOUNT_CREATION("Create New Account"), CREATING_ACCOUNT,
    LOGOUT, DISCONNECTED,
    GOTO_LOBBY, LOBBY,
    JOIN, NEW_GAME,
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
