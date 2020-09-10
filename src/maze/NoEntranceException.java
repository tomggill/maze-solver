package maze;

public class NoEntranceException extends InvalidMazeException {

    public NoEntranceException() {
        super("There is no entrance to the maze.");
    }

    public NoEntranceException(String message) {
        super(message);
    }
}