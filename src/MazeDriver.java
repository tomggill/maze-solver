import maze.Maze;
import maze.routing.RouteFinder;

/**
 * The MazeDriver class used to print Maze and RouteFinder state to the
 * console/terminal.
 * 
 * @author Thomas Gill
 * @version 20th March 2020
 */
public class MazeDriver {

    /** A constructor for MazeDriver. */
    public MazeDriver() {

    }

    public static void main(String args[]) {
        // Maze.fromTxt("maze1.txt");
        RouteFinder rf = new RouteFinder(Maze.fromTxt("../mazes/maze1.txt"));

        while (true) {
            if (rf.step() == true) {
                break;
            } else {
                System.out.println(rf.toString());
                continue;
            }
        }
        if (rf.isFinished()) {
            System.out.println("Finished");
        } else {
            System.out.println("Cannot be solved.");
        }
        rf.step();

    }
}