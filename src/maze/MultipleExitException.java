package maze;

public class MultipleExitException extends InvalidMazeException {

    public MultipleExitException() {
        super("There is more than one exit, there should only be one.");
    }

    public MultipleExitException(String message) {
        super(message);
    }
}