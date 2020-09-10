package maze;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Maze class used for instantiating a new Maze and provides methods for
 * retrieving and setting certain parts of the maze (such as a specific tile,
 * e.g entrance). It is an object that represents the maze to be solved.
 * 
 * @author Thomas Gill
 * @version 20th March 2020
 */
public class Maze implements Serializable {
    private List<List<Tile>> tiles = new ArrayList<List<Tile>>();;
    private Tile exit;
    private Tile entrance;

    /** Directions that can be used. */
    public enum Direction {
        NORTH, SOUTH, EAST, WEST;
    }

    /** A constructor for Maze. */
    private Maze() {
    }

    /**
     * Instantiates a maze instance and populates the 2D array list tiles according
     * to the .txt maze layout.
     * 
     * @param file: The file to read as a maze.
     * @return Returns the maze instance.
     * @throws FileNotFoundException     If the file does not exist, or it is
     *                                   inaccessible.
     * @throws IOException               If there is a problem with the input /
     *                                   output, for example, trying to read/write
     *                                   but don't have permission, or when the file
     *                                   is not found. FileNotFoundException is a
     *                                   subclass of IOException.
     * @throws MultipleEntranceException If the given maze file indicates that it
     *                                   has more than one entrance (indicated by
     *                                   the symbol 'e').
     * @throws MultipleExitException     If the given maze file indicates that it
     *                                   has more than one exit (indicated by the
     *                                   symbol 'x').
     * @throws NoEntranceExcepton        If the given maze file does not have any
     *                                   entrance tile (indicated by the symbol 'e'
     *                                   in the maze txt file).
     * @throws NoExitException           If the given maze file does not have any
     *                                   exite tile (indicated by the symbol 'x' in
     *                                   the maze txt file).
     * @throws RaggedMazeException       If the given maze txt file has differing
     *                                   size of rows.
     * @throws InvalidMazeException      If the given maze has an invalid maze
     *                                   format, such as an invalid char. Superclass
     *                                   of other exceptions.
     */
    public static Maze fromTxt(String file) {
        Maze maze = new Maze();
        ArrayList<Integer> raggedChecker = new ArrayList<Integer>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = bufferedReader.readLine();
            int rowCount = 0;
            while (line != null) {
                maze.tiles.add(new ArrayList<Tile>());

                for (int i = 0; i < line.length(); i++) {
                    maze.tiles.get(rowCount).add(i, Tile.fromChar(line.charAt(i)));
                    if (maze.tiles.get(rowCount).get(i) == null) {
                        throw new InvalidMazeException();
                    }

                    if (line.charAt(i) == 'e') {
                        maze.setEntrance(maze.tiles.get(rowCount).get(i));
                    } else if (line.charAt(i) == 'x') {
                        maze.setExit(maze.tiles.get(rowCount).get(i));
                    }
                }
                raggedChecker.add(line.length());
                line = bufferedReader.readLine();
                rowCount += 1;
            }

            // Checking whether the maze is ragged.
            for (int i = 1; i < raggedChecker.size(); i++) {
                if (raggedChecker.get(0) != raggedChecker.get(i)) {
                    throw new RaggedMazeException();
                }
            }
            // Checking whether the maze has an entrance and an exit.
            if (maze.getEntrance() == null) {
                throw new NoEntranceException();
            } else if (maze.getExit() == null) {
                throw new NoExitException();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not open " + file);
        } catch (IOException e) {
            System.out.println("Error: IOException when reading " + file);
        } catch (MultipleEntranceException e) {
            System.out.println(e);
            throw e;
        } catch (MultipleExitException e) {
            System.out.println(e);
            throw e;
        } catch (NoEntranceException e) {
            System.out.println(e);
            throw e;
        } catch (NoExitException e) {
            System.out.println(e);
            throw e;
        } catch (RaggedMazeException e) {
            System.out.println(e);
            throw e;
        } catch (InvalidMazeException e) {
            System.out.println(e);
            throw e;
        }
        return maze;
    }

    /**
     * Gets the tile adjacent to the one given, in the direction given.
     * 
     * @param t: The source tile.
     * @param d: The direction to go.
     * @return Returns the adjacent tile in the direction given from the source
     *         tile.
     */
    public Tile getAdjacentTile(Tile t, Direction d) {
        Coordinate oldTileCoordinate = this.getTileLocation(t); // Check if this is allowed because maze was declared
                                                                // privately.
        int col = oldTileCoordinate.getX();
        int row = (tiles.size() - 1 - oldTileCoordinate.getY());
        if (d == Direction.NORTH) {
            row -= 1;
        } else if (d == Direction.SOUTH) {
            row += 1;
        } else if (d == Direction.EAST) {
            col += 1;
        } else if (d == Direction.WEST) {
            col -= 1;
        }
        Coordinate newTileCoordinate = new Coordinate(col, tiles.size() - 1 - row);

        if ((col < 0) | (col >= tiles.get(0).size()) | (row < 0) | (row >= tiles.size())) {
            return Tile.fromChar('#'); // If the index is out of bounds return a tile which is not navigable.
        } else {
            return (this.getTileAtLocation(newTileCoordinate));
        }
    }

    /**
     * Gets the entrance tile of the maze.
     * 
     * @return Returns the entrance tile (and hence associated attributes).
     */
    public Tile getEntrance() {
        for (int row = 0; row < tiles.size(); row++) {
            for (int col = 0; col < tiles.get(0).size(); col++) {
                if (tiles.get(row).get(col).toString() == "e") {
                    return tiles.get(row).get(col);
                }
            }
        }
        return null;
    }

    /**
     * Gets the exit tile of the maze.
     * 
     * @return Returns the exit tile (and hence associated attributes).
     */
    public Tile getExit() {
        for (int row = 0; row < tiles.size(); row++) {
            for (int col = 0; col < tiles.get(0).size(); col++) {
                if (tiles.get(row).get(col).toString() == "x") {
                    return tiles.get(row).get(col);
                }
            }
        }
        return null;
    }

    /**
     * Gets the tile at a location given by the coord parameter.
     * 
     * @param coord: The coordinate instance representing a tiles coordinates.
     * @return Returns the tile at the given coordinates.
     */
    public Tile getTileAtLocation(Coordinate coord) {
        int col = coord.getX();
        int row = (tiles.size() - 1 - coord.getY());
        return tiles.get(row).get(col);
    }

    /**
     * Gets the coordinates of a given tile and creates a Coordinate instance using
     * these coordinates.
     * 
     * @param t: A specific tile on the maze.
     * @return Returns the coordinate instance of that particular tile.
     */
    public Coordinate getTileLocation(Tile t) {
        for (int row = 0; row < tiles.size(); row++) {
            for (int col = 0; col < tiles.get(0).size(); col++) {
                if (tiles.get(row).get(col) == t) {
                    Coordinate coord = new Coordinate(col, tiles.size() - 1 - row);
                    return coord;
                }
            }
        }
        Coordinate coord = new Coordinate(0, 0); // If the tile is not found.
        return coord;
    }

    /**
     * Gets the maze's tiles attribute.
     * 
     * @return Returns the (2D array list) tiles attribute.
     */
    public List<List<Tile>> getTiles() {
        return tiles;
    }

    /**
     * Sets the entrance attribute (to a given tile).
     * 
     * @param t: A specific tile on the maze.
     * @throws MultipleEntranceException If the given maze file indicates that it
     *                                   has more than one entrance (indicated by
     *                                   the symbol 'e').
     */
    private void setEntrance(Tile t) {
        for (List<Tile> row : this.tiles) {
            for (Tile element : row) {
                if (element == t) {
                    if ((this.entrance == null)) {
                        this.entrance = t;
                    } else {
                        // If a second entrance.
                        throw new MultipleEntranceException();
                    }
                }
            }
        }
    }

    /**
     * Sets the exit attribute (to a given tile).
     * 
     * @param t: A specific tile on the maze.
     * @throws MultipleExitException If the given maze file indicates that it has
     *                               more than one exit (indicated by the symbol
     *                               'x').
     */
    private void setExit(Tile t) {
        for (List<Tile> row : this.tiles) {
            for (Tile element : row) {
                if (element == t) {
                    if (this.exit == null) {
                        this.exit = t;
                    } else {
                        // If a second exit.
                        throw new MultipleExitException();
                    }
                }
            }
        }
    }

    /**
     * Converts the maze instance into a string format.
     * 
     * @return Returns a string representation of the maze.
     */
    public String toString() {
        String mazeVisual = "";
        for (int row = 0; row < tiles.size(); row++) {
            for (int col = 0; col < tiles.get(0).size(); col++) {
                mazeVisual = (mazeVisual + tiles.get(row).get(col).toString());
                if ((col + 1) == tiles.get(0).size()) {
                    mazeVisual = mazeVisual + "\n";
                }
            }
        }
        return mazeVisual;
    }

    /**
     * The Coordinate class is used to allow a position in the maze to be
     * represented as an object with attributes. These attributes are its column and
     * row number.
     * 
     * @author Thomas Gill
     * @version 20th March 2020
     */
    public class Coordinate {
        private int x; // x is col
        private int y; // y is row

        /**
         * A constructor for Coordinate.
         * 
         * @param col: The column number of a coordinate.
         * @param row: The row number of a coordinate.
         */
        public Coordinate(int col, int row) {
            this.x = col;
            this.y = row;
        }

        /**
         * Gets the column number of a Coordinate.
         * 
         * @return Returns an integer representing the column number.
         */
        public int getX() {
            return x;
        }

        /**
         * Gets the row number of a Coordinate.
         * 
         * @return Returns an integer representing the row number.
         */
        public int getY() {
            return y;
        }

        /**
         * Gets the Coordinate's column and row number, putting them together into a
         * string.
         * 
         * @return Returns a string representation of the Coordinate.
         */
        public String toString() {
            return ("(" + x + ", " + y + ")");
        }
    }

}