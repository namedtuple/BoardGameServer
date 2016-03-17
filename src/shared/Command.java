package shared;

public enum Command {

    // Constants
    LOGGING_IN, LOGIN_SUCCESS, GOTO_LOGIN, LOGIN_FAIL("Unable to log in to game server. Check that your username/password is correct."),
    ACCOUNT_CREATION, CREATING_ACCOUNT, ACCOUNT_CREATION_FAIL("Unable to create new account. Username already exists."),
    GET_PROFILE, PROFILE,
    LOGOUT, DISCONNECTED,
    GOTO_LOBBY, LOBBY,
    JOIN, NEW_GAME,
    MOVE, VALID_MOVE("It is your opponent's turn"), OPPONENT_MOVED("It is your turn"),
    YOUR_TURN, OPPONENT_TURN,
    CONTINUE_JUMP, //for checkers
    VICTORY("You win!"), DEFEAT("You lose!"), TIE("You tied!"),
    MOVE_TO,
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
