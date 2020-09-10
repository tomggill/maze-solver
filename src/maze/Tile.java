package maze;

import java.io.Serializable;

/**
 * The Tile class that represents a particular Tile which may be on the maze.
 * 
 * @author Thomas Gill
 * @version 20th March 2020
 */
public class Tile implements Serializable {
    private Type type;

    /** Tile types that can be used. */
    public enum Type {
        CORRIDOR, ENTRANCE, EXIT, WALL;
    }

    /** A constructor for Tile. */
    private Tile(Type t) {
        this.type = t;
    }

    /**
     * Creates a new tile instance depending on what the character on the maze is,
     * and returns the tile instance with the correct Type.
     * 
     * @param c: The particular character shown in the maze file.
     * @return Returns the a new Tile instance.
     */
    protected static Tile fromChar(char c) {
        if (c == '.') {
            return new Tile(Type.CORRIDOR);
        } else if (c == 'e') {
            return new Tile(Type.ENTRANCE);
        } else if (c == 'x') {
            return new Tile(Type.EXIT);
        } else if (c == '#') { // When the character is equal to '#'.
            return new Tile(Type.WALL);
        } else {
            return null;
        }
    }

    /**
     * Gets the Type of a Tile object.
     * 
     * @return Returns the Type of a Tile object.
     */
    public Type getType() {
        return type;
    }

    /**
     * Checks whether a tile is navigable by checking that it is not a WALL Type.
     * 
     * @return Returns true if the tile object is not a wall and hence is navigable.
     */
    public boolean isNavigable() {
        if (this.type == Type.WALL) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Gets the string representation of a Type used in text files (maze files).
     * 
     * @return Returns a string representation of the Tile objects Type.
     */
    public String toString() {
        if (this.type == Type.CORRIDOR) {
            return ".";
        } else if (this.type == Type.ENTRANCE) {
            return "e";
        } else if (this.type == Type.EXIT) {
            return "x";
        } else { // When the tile is a wall type.
            return "#";
        }
    }
}