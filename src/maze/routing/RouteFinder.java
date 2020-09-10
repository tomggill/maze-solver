package maze.routing;

import maze.Maze;
import maze.Maze.Direction;
import maze.Tile;
import maze.Tile.Type;
import java.util.Stack;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * The RouteFinder class provides the core logic for solving a given maze. It
 * provides methods for stepping through the maze solving state, loading and
 * saving routes, and providing a toString method to return a string
 * representation of the maze.
 * 
 * @author Thomas Gill
 * @version 24th April 2020
 */
public class RouteFinder implements Serializable {
    private Maze maze;
    private Stack<Tile> route = new Stack<Tile>();
    private boolean finished;
    private Stack<Tile> popped = new Stack<Tile>(); // Not on UML diagram.

    /** A constructor for creating RouteFinder objects. */
    public RouteFinder(Maze m) {
        maze = m;
    }

    /**
     * Gets the maze object of this RouteFinder object.
     * 
     * @return Returns the maze object for the RouteFinder.
     */
    public Maze getMaze() {
        return maze;
    }

    /**
     * Gets the current route solving state for the maze.
     * 
     * @return Returns the a list of Tiles representing the current route, where the
     *         first value in the list is the entrance.
     */
    public List<Tile> getRoute() {
        List<Tile> currentRoute = new ArrayList<Tile>();
        for (int i = 0; i < route.size(); i++) {
            currentRoute.add(route.get(i));
        }
        return currentRoute;
    }

    /**
     * Checks whether the maze has been solved.
     * 
     * @return Returns the value of the finished attribute.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Allows RouteFinder objects to be loaded from a file, along with its state
     * when it was saved.
     * 
     * @param s: The name of the file to be loaded
     * @return Returns the instance of RouteFinder loaded from the file.
     * @throws FileNotFoundException    If the file does not exist, or it is
     *                                  inaccessible.
     * @throws ClassNotFoundException   If the program is trying to read an object
     *                                  of an unknown class.
     * @throws StreamCorruptedException Thrown when control information that was
     *                                  read from an object stream violates internal
     *                                  consistency checks.
     * @throws IOException              If there is a problem with the input /
     *                                  output, for example, trying to read/write
     *                                  but don't have permission, or when the file
     *                                  is not found. FileNotFoundException is a
     *                                  subclass of IOException.
     */
    public static RouteFinder load(String s) {
        try (FileInputStream routeFile = new FileInputStream(s);
                ObjectInputStream routeStream = new ObjectInputStream(routeFile);) {
            RouteFinder rfTemp = (RouteFinder) routeStream.readObject();
            return rfTemp;
        } catch (FileNotFoundException e) {
            System.out.println("\nNo file was read");
        } catch (ClassNotFoundException e) {
            System.out.println("\nTrying to read an object of an unknown class");
        } catch (StreamCorruptedException e) {
            System.out.println("\nUnreadable file format");
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Allows RouteFinder objects to be serialised out to a file, along with its
     * state when it is saved.
     * 
     * @param s: The name of the file to be loaded
     * @return Returns the instance of RouteFinder loaded from the file.
     * @throws IOException If there is a problem with the input / output, for
     *                     example, trying to read/write but don't have permission,
     *                     or when the file is not found. FileNotFoundException is a
     *                     subclass of IOException.
     */
    public void save(String s) {
        try (FileOutputStream routeFile = new FileOutputStream(s);
                ObjectOutputStream routeStream = new ObjectOutputStream(routeFile);) {
            routeStream.writeObject(this);

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Updates the stack that is resposible for holding the routefinding state. A
     * call to the step method makes exactly one move through the maze.
     * 
     * @return Returns true is the maze has been solved.
     * @throws NoRouteFoundException If the maze has a circular route or there is no
     *                               route the the exist possible.
     */
    public boolean step() {
        Tile nextTile;
        Direction d;
        int directionCount = 0;
        try {
            if (finished == true) {
                return true;
            }
            if (route.empty()) {
                route.push(maze.getEntrance());
                return false;
            } else {
                while (true) {
                    if (directionCount == 0) {
                        d = Direction.NORTH;
                    } else if (directionCount == 1) {
                        d = Direction.EAST;
                    } else if (directionCount == 2) {
                        d = Direction.SOUTH;
                    } else if (directionCount == 3) {
                        d = Direction.WEST;
                    } else {
                        if (!popped.isEmpty()) {
                            if (popped.peek().getType() == Type.ENTRANCE) {
                                throw new NoRouteFoundException();
                            }
                        }
                        popped.push(route.pop());
                        break;
                    }
                    nextTile = maze.getAdjacentTile(route.peek(), d);
                    if (nextTile.isNavigable() && !route.contains(nextTile) && !popped.contains(nextTile)) {
                        route.push(maze.getAdjacentTile(route.peek(), d));
                        break;
                    } else {
                        directionCount += 1;
                        continue;
                    }
                }

                if (!route.isEmpty()) {
                    if (route.peek().getType() == Type.EXIT) {
                        finished = true;
                        return true;
                    }
                }
                return false;

            }
        } catch (NoRouteFoundException e) {
            System.out.println(e);
            throw e;
        }
    }

    /**
     * Converts the entire maze and route solving state to a string
     * 
     * @return Returns a string representation of the maze and the route solving
     *         state.
     */
    public String toString() {
        String mazeVisual = "";
        Maze routeMaze = this.getMaze();
        for (int row = 0; row < routeMaze.getTiles().size(); row++) {
            for (int col = 0; col < routeMaze.getTiles().get(0).size(); col++) {
                if (route.contains(routeMaze.getTiles().get(row).get(col))) {
                    mazeVisual = (mazeVisual + "*");
                } else if (popped.contains(routeMaze.getTiles().get(row).get(col))) {
                    mazeVisual = (mazeVisual + "-");
                } else {
                    mazeVisual = (mazeVisual + routeMaze.getTiles().get(row).get(col).toString());
                }
                if ((col + 1) == routeMaze.getTiles().get(0).size()) {
                    mazeVisual = mazeVisual + "\n";
                }
            }
        }
        return mazeVisual;
    }
}