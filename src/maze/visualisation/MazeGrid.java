package maze.visualisation;

import maze.Maze;
import maze.routing.RouteFinder;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.geometry.HPos;

/**
 * The MazeGrid class is used for providing a javafx component to be used in
 * maze visualising programs.
 * 
 * @author Thomas Gill
 * @version 24th April 2020
 */
public class MazeGrid extends GridPane {
    private RouteFinder routeFinder;
    private Color pathColour = Color.BLUE;

    /** A constructor for creating MazeGrid objects. */
    public MazeGrid(RouteFinder rf) {
        this.routeFinder = rf;
        this.clearMaze();
        this.updateMaze(rf);
        super.setLayoutX(10);
        super.setLayoutY(110);
        super.setStyle("-fx-border-style: solid;\n-fx-border-width: 2;\n");
    }

    /**
     * Allows the updating of the maze representation using the RouteFinder object.
     * 
     * @param rf: The RouteFinder object to visualise.
     */
    public void updateMaze(RouteFinder rf) {
        String mazeRepresentation = rf.toString();
        Rectangle rect = null;
        int col = 0;
        int row = 0;
        this.routeFinder = rf;
        for (int i = 0; i < mazeRepresentation.length(); i++) {
            char tileChar = mazeRepresentation.charAt(i);
            rect = new Rectangle();
            rect.setWidth(25);
            rect.setHeight(25);
            if (tileChar == '*') {
                rect.setFill(this.getPathColour());
                rect.setStroke(this.getPathColour());
                this.add(rect, col, row);
                GridPane.setHalignment(rect, HPos.CENTER);
                col += 1;
            } else if (tileChar == 'e') {
                rect.setFill(Color.GREEN);
                rect.setStroke(Color.GREEN);
                this.add(rect, col, row);
                super.setHalignment(rect, HPos.CENTER);
                col += 1;
            } else if (tileChar == '.') {
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.WHITE);
                this.add(rect, col, row);
                GridPane.setHalignment(rect, HPos.CENTER);
                col += 1;
            } else if (tileChar == 'x') {
                rect.setFill(Color.RED);
                rect.setStroke(Color.RED);
                this.add(rect, col, row);
                GridPane.setHalignment(rect, HPos.CENTER);
                col += 1;
            } else if (tileChar == '-') {
                rect.setFill(Color.GRAY);
                rect.setStroke(Color.GRAY);
                this.add(rect, col, row);
                GridPane.setHalignment(rect, HPos.CENTER);
                col += 1;
            } else if (tileChar == '#') {
                rect.setStroke(Color.BLACK);
                this.add(rect, col, row);
                GridPane.setHalignment(rect, HPos.CENTER);
                col += 1;
            } else if (tileChar == '\n') {
                row += 1;
                col = 0;
            }
        }
    }

    /**
     * Sets the colour for the path of the MazeGrid visualisation and updates it.
     * 
     * @param colour: The colour the path of the MazeGrid is changing to.
     */
    public void setPathColour(Color colour) {
        this.pathColour = colour;
        this.updateMaze(routeFinder);
    }

    /**
     * Sets the colour for the path of the MazeGrid visualisation and updates it.
     * 
     * @return Returns the colour of the path of the MazeGrid.
     */
    public Color getPathColour() {
        return pathColour;
    }

    /**
     * Clears the MazeGrid objects' components
     */
    public void clearMaze() {
        super.getChildren().clear();
    }
}