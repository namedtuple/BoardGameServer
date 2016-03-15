package client;

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

    public String getRequest() {
        return request;
    }

    public Command getCommand() {
        return command;
    }

}
