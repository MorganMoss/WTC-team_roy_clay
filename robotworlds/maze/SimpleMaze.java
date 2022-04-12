package za.co.wethinkcode.robotworlds.maze;

import za.co.wethinkcode.robotworlds.Position;
import za.co.wethinkcode.robotworlds.world.*;

import java.util.ArrayList;
import java.util.List;

public class SimpleMaze implements Maze {

    @Override
    public List<Obstacle> getObstacles() {
        List obstacle =new ArrayList();
        obstacle.add(new SquareObstacle(1,1));


        return obstacle;
    }


    @Override
    public boolean blocksPath(Position a, Position b) {
        return false;
    }
}
