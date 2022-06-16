package za.co.wethinkcode.client;

import za.co.wethinkcode.robotworlds.maze.Maze;

//This can perhaps be used as a client, but for now, this is way out of spec.
//TODO: Remove this class server-side, but keep it for clientside
public class TextWorld extends AbstractWorld{

    public TextWorld (Maze maze){
        super(maze);
        showObstacles(maze.getObstacles());
    }

}