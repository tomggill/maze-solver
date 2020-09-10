package maze;

public class NoExitException extends InvalidMazeException {

    public NoExitException() {
        super("There is no exit to the maze.");
    }

    public NoExitException(String message) {
        super(message);
    }
}