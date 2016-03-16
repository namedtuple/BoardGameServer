package shared;

public class Request {

    // Fields
    private String request;
    private Command command;

    // Methods
    public Request(String request) {
        this.request = request;
        String commandStr = request.split(" ")[0];
        if (request.startsWith("You said: ")) {
            this.command = Command.NULL;
        } else {
            this.command = Command.valueOf(commandStr);
        }
    }

    public Request(Command command) {
        this.request = command.toString();
        this.command = command;
    }

    public Request(Command command, String commandParameters) {
        this.request = command.toString() + " " + commandParameters;
        this.command = command;
    }

    public String getRequest() {
        return request;
    }

    public Command getCommand() {
        return command;
    }

    public String[] getTokens() {
        return request.split(" ");
    }

}
