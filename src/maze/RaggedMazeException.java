package maze;

public class RaggedMazeException extends InvalidMazeException {

    public RaggedMazeException() {
        super("The maze is not a rectangle (the edges are not straight).");
    }

    public RaggedMazeException(String message) {
        super(message);
    }
}