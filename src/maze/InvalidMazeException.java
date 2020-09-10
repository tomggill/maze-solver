package maze;

public class InvalidMazeException extends RuntimeException {

    public InvalidMazeException() {
        super("Error in the maze input.");
    }

    public InvalidMazeException(String message) {
        super(message);
    }
}