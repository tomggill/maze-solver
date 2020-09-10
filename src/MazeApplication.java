import maze.Maze;
import maze.routing.RouteFinder;
import maze.visualisation.MazeGrid;
import maze.InvalidMazeException;
import maze.routing.NoRouteFoundException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import javafx.scene.control.ComboBox;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.geometry.HPos;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;

public class MazeApplication extends Application {
    private RouteFinder rf;
    private MazeGrid mazeGrid;

    public MazeApplication() {
    }

    @Override
    public void start(Stage stage) {
        Pane p = new Pane();

        HBox hb1 = new HBox(10);
        hb1.setLayoutX(20);
        hb1.setLayoutY(20);

        Alert finishAlert = new Alert(AlertType.INFORMATION);
        finishAlert.setHeaderText("MAZE COMPLETE");
        finishAlert.setContentText("CONGRATULATIONS, YOU HAVE COMPLETED THE MAZE.");

        Alert loadRouteErrorAlert = new Alert(AlertType.ERROR);
        loadRouteErrorAlert.setHeaderText("File not valid.");
        loadRouteErrorAlert.setContentText("File must be a valid .route file containing a RouteFinder object.");

        Alert loadMapErrorAlert = new Alert(AlertType.ERROR);
        loadMapErrorAlert.setHeaderText("File not valid.");

        Alert noRouteAlert = new Alert(AlertType.ERROR);
        noRouteAlert.setHeaderText("File not valid.");

        Button b1 = new Button("Load map");

        Button b2 = new Button("Load route");

        Button b3 = new Button("Save route");
        b3.setDisable(true);

        Button b4 = new Button("Step");
        b4.setDisable(true);

        Button b5 = new Button("Multi Step");
        b5.setDisable(true);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Text Input Dialog");
        dialog.setHeaderText("You are stepping");
        dialog.setContentText("Please enter the number of steps you want jump:");

        FileChooser fileChooser1 = new FileChooser();
        fileChooser1.setInitialDirectory(new File("../mazes/"));
        fileChooser1.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        FileChooser fileChooser2 = new FileChooser();
        fileChooser2.setInitialDirectory(new File("../routes/"));
        fileChooser2.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Route Files", "*.route"));

        FileChooser fileChooser3 = new FileChooser();
        fileChooser3.setInitialDirectory(new File("../routes/"));
        fileChooser3.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Route Files", "*.route"));

        ComboBox<Integer> cbNumbers = new ComboBox<Integer>();
        cbNumbers.getItems().addAll(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        cbNumbers.setDisable(true);

        ComboBox<String> cbColour = new ComboBox<String>();
        cbColour.getItems().addAll("ORANGERED", "ORANGE", "YELLOW", "GREEN", "BLUE", "INDIGO", "VIOLET");
        cbColour.setValue("BLUE");
        cbColour.setDisable(true);

        b1.setOnAction(e -> {
            File selectedFile = fileChooser1.showOpenDialog(stage);
            if (selectedFile != null) {
                try {
                    rf = new RouteFinder(Maze.fromTxt(selectedFile.getAbsolutePath()));
                    if (mazeGrid != null) {
                        mazeGrid.clearMaze();
                    }
                    mazeGrid = new MazeGrid(rf);
                    mazeGrid.updateMaze(rf);
                    b3.setDisable(false);
                    b4.setDisable(false);
                    b5.setDisable(false);
                    cbColour.setDisable(false);
                    cbColour.setValue("BLUE");
                    p.getChildren().add(mazeGrid);
                } catch (InvalidMazeException error) {
                    loadMapErrorAlert.setContentText("Error: " + error);
                    loadMapErrorAlert.showAndWait();
                }
            }
        });

        b2.setOnAction(e -> {
            File selectedFile = fileChooser2.showOpenDialog(stage);
            if (selectedFile != null) {
                rf = RouteFinder.load(selectedFile.getAbsolutePath());
                if (rf == null) {
                    loadRouteErrorAlert.showAndWait();
                } else {
                    b3.setDisable(false);
                    b4.setDisable(false);
                    b5.setDisable(false);
                    cbColour.setDisable(false);
                    cbColour.setValue("BLUE");
                    if (mazeGrid != null) {
                        mazeGrid.clearMaze();
                        mazeGrid.updateMaze(rf);
                    } else {
                        mazeGrid = new MazeGrid(rf);
                        mazeGrid.updateMaze(rf);
                        p.getChildren().add(mazeGrid);
                    }
                }
            }
        });

        b3.setOnAction(e -> {
            File selectedFile = fileChooser3.showSaveDialog(stage);
            if (selectedFile != null) {
                rf.save(selectedFile.getAbsolutePath());
            }
        });

        b4.setOnAction(e -> {
            try {
                if (rf.step() == true) {
                    finishAlert.show();
                } else {
                    mazeGrid.clearMaze();
                    mazeGrid.updateMaze(rf);
                }
            } catch (NoRouteFoundException error) {
                noRouteAlert.setContentText("Error: " + error);
                noRouteAlert.showAndWait();
            }
        });

        b5.setOnAction(e -> {
            b1.setDisable(true);
            b2.setDisable(true);
            b3.setDisable(true);
            b4.setDisable(true);
            b5.setDisable(true);
            cbNumbers.setDisable(false);
        });

        cbNumbers.setOnAction(e -> {
            int numOfSteps = cbNumbers.getSelectionModel().getSelectedItem();
            try {
                if ((cbNumbers.getValue() != null)) {
                    for (int i = 0; i < (numOfSteps); i++) {
                        if (rf.step() == true) {
                            finishAlert.show();
                            break;
                        } else {
                            mazeGrid.clearMaze();
                            mazeGrid.updateMaze(rf);
                        }
                    }
                }

            } catch (NoRouteFoundException error) {
                noRouteAlert.setContentText("Error: " + error);
                noRouteAlert.showAndWait();
            } finally {
                b1.setDisable(false);
                b2.setDisable(false);
                b3.setDisable(false);
                b4.setDisable(false);
                b5.setDisable(false);
                cbNumbers.setDisable(true);
                cbNumbers.valueProperty().set(0);

            }
        });

        cbColour.setOnAction(e -> {
            String colour = cbColour.getSelectionModel().getSelectedItem();
            if (cbColour.getValue() != null) {
                if (colour == "ORANGERED") {
                    mazeGrid.setPathColour(Color.ORANGERED);
                } else if (colour == "ORANGE") {
                    mazeGrid.setPathColour(Color.ORANGE);
                } else if (colour == "YELLOW") {
                    mazeGrid.setPathColour(Color.YELLOW);
                } else if (colour == "GREEN") {
                    mazeGrid.setPathColour(Color.GREEN);
                } else if (colour == "BLUE") {
                    mazeGrid.setPathColour(Color.BLUE);
                } else if (colour == "INDIGO") {
                    mazeGrid.setPathColour(Color.INDIGO);
                } else if (colour == "VIOLET") {
                    mazeGrid.setPathColour(Color.VIOLET);
                }
            }
        });

        hb1.getChildren().addAll(b1, b2, b3, b4, b5, cbNumbers, cbColour);
        p.getChildren().addAll(hb1);
        Scene scene = new Scene(p, 800, 600);
        stage.setScene(scene);
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            b1.setPrefWidth(stage.getWidth() / 8);
            b2.setPrefWidth(stage.getWidth() / 8);
            b3.setPrefWidth(stage.getWidth() / 8);
            b4.setPrefWidth(stage.getWidth() / 8);
            b5.setPrefWidth(stage.getWidth() / 8);
            cbNumbers.setPrefWidth(stage.getWidth() / 8);
            cbColour.setPrefWidth(stage.getWidth() / 8);

        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            b1.setPrefHeight(stage.getHeight() / 10);
            b2.setPrefHeight(stage.getHeight() / 10);
            b3.setPrefHeight(stage.getHeight() / 10);
            b4.setPrefHeight(stage.getHeight() / 10);
            b5.setPrefHeight(stage.getHeight() / 10);
        });
        stage.setTitle("Maze");
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
}