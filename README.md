# Maze-Solver

The goal of this project was to create a JavaFX application that programatically solves, and visualises mazes, as a means to improve my skills with Java and JavaFX. It was also to help me improve my skills using various data structures and error handling.

## Running the program

These commands need to be run in the /src directory.

To run the program,

Compile:

    javac --module-path ./lib/ --add-modules=javafx.controls MazeApplication.java

Run:

    java --module-path ./lib/ --add-modules=javafx.controls MazeApplication

To run the tests,

    ./tests.sh

## Program features

Key:

- Starting tile is coloured green.
- End tile is coloured red.
- Tiles not stepped through are coloured white.
- Borders are coloured black.
- Tiles stepped through but not part of final route are coloured grey.
- You can change colour of tiles part of the final route through the dropdown menu.

You can save the state of your half-solved maze so that you can load it again and pickup where you left off at another time.
