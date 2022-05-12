package za.co.wethinkcode.robotworlds.world;

import za.co.wethinkcode.robotworlds.Position;
import za.co.wethinkcode.robotworlds.Robot;
import za.co.wethinkcode.robotworlds.maze.Maze;

import java.util.List;

public class AbstractWorld implements IWorld{
    protected Position TOP_LEFT = new Position(-100,200);
    protected Position BOTTOM_RIGHT = new Position(100,-200);

    public static final Position CENTRE = new Position(0,0);
    protected Position position;
    protected Direction currentDirection;
    protected final List<Obstacle> obstacles;
    protected final Maze maze;

    @Override
    public void SetPositions(int tlx, int tly, int blx, int bly){
        TOP_LEFT = new Position(tlx,tly);
        BOTTOM_RIGHT = new Position(blx,bly);
    }
    public AbstractWorld(Maze maze){
        this.maze = maze;
        this.position = IWorld.CENTRE;
        this.currentDirection = Direction.UP;
        this.obstacles= this.maze.getObstacles();
    }


    @Override
    public UpdateResponse updatePosition(int nrSteps) {
        int newX = this.position.getX();
        int newY = this.position.getY();

        if (Direction.UP.equals(this.currentDirection)) {
            newY = newY + nrSteps;
        }else if(Direction.DOWN.equals(this.currentDirection)) {
            newY = newY - nrSteps;
        }else if(Direction.LEFT.equals(this.currentDirection)) {
            newX = newX - nrSteps;
        }else if(Direction.RIGHT.equals(this.currentDirection)) {
            newX = newX + nrSteps;
        }

        Position newPosition = new Position(newX, newY);
        for (Obstacle obstacle :obstacles){
            if (obstacle.blocksPath(this.position,newPosition)){
                return UpdateResponse.FAILED_OBSTRUCTED;
            }
        }

        if (newPosition.isIn(TOP_LEFT,BOTTOM_RIGHT)){
            this.position = newPosition;
            return UpdateResponse.SUCCESS;
        }
        return  UpdateResponse.FAILED_OUTSIDE_WORLD;
    }


    @Override
    public void updateDirection(boolean turnRight) {
        if (turnRight == true) {
            if (Direction.UP.equals(this.currentDirection)) {
                this.currentDirection = Direction.RIGHT;
            } else if (Direction.DOWN.equals(this.currentDirection)) {
                this.currentDirection = Direction.LEFT;
            } else if (Direction.LEFT.equals(this.currentDirection)) {
                this.currentDirection = Direction.UP;
            } else if (Direction.RIGHT.equals(this.currentDirection)) {
                this.currentDirection = Direction.DOWN;
            }
        } else if (turnRight == false) {
            if (Direction.UP.equals(this.currentDirection)) {
                this.currentDirection = Direction.LEFT;
            } else if (Direction.DOWN.equals(this.currentDirection)) {
                this.currentDirection = Direction.RIGHT;
            } else if (Direction.LEFT.equals(this.currentDirection)) {
                this.currentDirection = Direction.DOWN;
            } else if (Direction.RIGHT.equals(this.currentDirection)) {
                this.currentDirection = Direction.DOWN;
            }
        }
    }


    @Override
    public Position getPosition() {
        return this.position;
    }


    @Override
    public Position resetPosition(int nrSteps) {
        int newX = this.position.getX();
        int newY = this.position.getY();

        if (Direction.UP.equals(this.currentDirection)) {
            newY = newY - nrSteps;
        }else if(Direction.DOWN.equals(this.currentDirection)) {
            newY = newY + nrSteps;
        }else if(Direction.LEFT.equals(this.currentDirection)) {
            newX = newX + nrSteps;
        }else if(Direction.RIGHT.equals(this.currentDirection)) {
            newX = newX - nrSteps;
        }

        return this.position = new Position(newX, newY);
    }


    @Override
    public Direction getCurrentDirection() {
        return this.currentDirection;
    }


    @Override
    public boolean isNewPositionAllowed(Position position) {
        return position.isIn(TOP_LEFT,BOTTOM_RIGHT);
    }

    @Override
    public boolean isAtEdge() {
        return this.position.getX() == TOP_LEFT.getX()
                || this.position.getY() == TOP_LEFT.getY()
                || this.position.getX() == BOTTOM_RIGHT.getX()
                || this.position.getY() == BOTTOM_RIGHT.getY();
    }
    @Override
    public void reset() {
        this.position = IWorld.CENTRE;
        this.currentDirection = Direction.UP;

    }


    @Override
    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }


    @Override
    public void showObstacles() {
        List<Obstacle> newObstacle= this.getObstacles();
        System.out.println("There are some obstacles :");
        System.out.println(getObstacles());
        System.out.println(this.obstacles);

        for (int i = 0; i < newObstacle.size(); i++) {
            int x= newObstacle.get(i).getBottomLeftX();
            int y= newObstacle.get(i).getBottomLeftY();
            System.out.println("- At position"+ x+","+y+" (to"+ x+4+","+y+4+")");
        }
    }


    @Override
    public void removeObstacle(Robot target){
        for (int i = 0; i <getObstacles().size(); i++) {
            System.out.println(getObstacles().get(i));
            if (target.getWorld().getPosition() ==getObstacles().get(i)){
                getObstacles().remove(i);
            }
        }
    }


    public void showObstacles(List<Obstacle> maze) {
        List<Obstacle> newObstacle= maze;
        System.out.println("There are some obstacles :");
        for (int i = 0; i < newObstacle.size(); i++) {
           int x= newObstacle.get(i).getBottomLeftX();
           int y= newObstacle.get(i).getBottomLeftY();

            System.out.println("- At position "+ x+", "+y+" (to "+ Math.addExact(x,4)+","+Math.addExact(y,4)+")");
        }
    }
}