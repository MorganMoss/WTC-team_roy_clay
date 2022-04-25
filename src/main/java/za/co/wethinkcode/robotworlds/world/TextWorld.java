package za.co.wethinkcode.robotworlds.world;

import za.co.wethinkcode.robotworlds.maze.Maze;

public class TextWorld extends AbstractWorld{

    public TextWorld (Maze maze){
        super(maze);
        showObstacles(maze.getObstacles());
    }

}