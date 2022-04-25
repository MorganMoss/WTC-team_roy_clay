package za.co.wethinkcode.robotworlds.maze;

import za.co.wethinkcode.robotworlds.Position;
import za.co.wethinkcode.robotworlds.world.Obstacle;

import java.util.ArrayList;
import java.util.List;

public class EmptyMaze implements Maze {

    List obstacle ;
    public EmptyMaze(){
      this.obstacle=new ArrayList<>();
    }

    @Override
    public List<Obstacle> getObstacles() {
        return this.obstacle;
    }

    @Override
    public boolean blocksPath(Position a, Position b) {
        return false;
    }

}
