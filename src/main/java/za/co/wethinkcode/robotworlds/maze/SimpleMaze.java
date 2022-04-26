package za.co.wethinkcode.robotworlds.maze;

import za.co.wethinkcode.robotworlds.Position;
import za.co.wethinkcode.robotworlds.Robot;
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



//    public void removeObstacle(Robot target){
//        for (int i = 0; i <getObstacles().size(); i++) {
//            System.out.println(getObstacles().get(i));
//            if (target.getWorld().getPosition() ==getObstacles().get(i)){
//                getObstacles().remove(i);
//            }
//        }
//    }


    @Override
    public boolean blocksPath(Position a, Position b) {
        return false;
    }
}
