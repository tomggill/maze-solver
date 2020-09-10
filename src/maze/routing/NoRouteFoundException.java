package maze.routing;

public class NoRouteFoundException extends RuntimeException {

    public NoRouteFoundException() {
        super("Circular route, or no route to the exit possible.");
    }

    public NoRouteFoundException(String message) {
        super(message);
    }
}