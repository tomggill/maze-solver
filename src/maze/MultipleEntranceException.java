package maze;

public class MultipleEntranceException extends InvalidMazeException {

    public MultipleEntranceException() {
        super("There is more than one entrance, there should only be one.");
    }

    public MultipleEntranceException(String message) {
        super(message);
    }
}