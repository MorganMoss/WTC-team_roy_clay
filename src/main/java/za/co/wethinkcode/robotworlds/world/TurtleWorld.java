package za.co.wethinkcode.robotworlds.world;

import za.co.wethinkcode.robotworlds.Position;
import za.co.wethinkcode.robotworlds.maze.Maze;

import java.util.List;
import za.co.wethinkcode.robotworlds.Turtle.Turtle;

public class TurtleWorld extends AbstractWorld {
    Turtle tmt = new Turtle();;
    private final List<Obstacle> obstacles;

    public TurtleWorld(Maze maze){
        super(maze);
        this.obstacles= maze.getObstacles();
        showObstacles(maze.getObstacles());
        mazeBoader();
        drawObstacles(maze);
        tmt.dot("orange");
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
            tmt.down();
            tmt.forward(nrSteps);
            return UpdateResponse.SUCCESS;
        }
        return  UpdateResponse.FAILED_OUTSIDE_WORLD;
    }


    @Override
    public void updateDirection(boolean turnRight) {
        if (turnRight == true) {
            if (Direction.UP.equals(this.currentDirection)) {
                this.currentDirection = Direction.RIGHT;
                tmt.right(90);
            } else if (Direction.DOWN.equals(this.currentDirection)) {
                this.currentDirection = Direction.LEFT;
                tmt.right(90);
            } else if (Direction.LEFT.equals(this.currentDirection)) {
                this.currentDirection = Direction.UP;
                tmt.right(90);
            } else if (Direction.RIGHT.equals(this.currentDirection)) {
                this.currentDirection = Direction.DOWN;
                tmt.right(90);
            }
        } else if (turnRight == false) {
            if (Direction.UP.equals(this.currentDirection)) {
                this.currentDirection = Direction.LEFT;
                tmt.left(90);
            } else if (Direction.DOWN.equals(this.currentDirection)) {
                this.currentDirection = Direction.RIGHT;
                tmt.left(90);
            } else if (Direction.LEFT.equals(this.currentDirection)) {
                this.currentDirection = Direction.DOWN;
                tmt.left(90);
            } else if (Direction.RIGHT.equals(this.currentDirection)) {
                this.currentDirection = Direction.DOWN;
                tmt.left(90);
            }
        }
    }


    @Override
    public void showObstacles() {
        List<Obstacle> newObstacle= this.getObstacles();
        System.out.println("There are some obstacles :");

        for (int i = 0; i < newObstacle.size(); i++) {
            int x= newObstacle.get(i).getBottomLeftX();
            int y= newObstacle.get(i).getBottomLeftY();

            System.out.println("- At position"+ x+","+y+" (to"+ x+4+","+y+4+")");;

        }

    }


    public void drawObstacles(Maze maze){
        List<Obstacle> newObstacle= maze.getObstacles();
        for (int i = 0; i < newObstacle.size(); i++) {

            tmt.speed(0);
            tmt.penColor("blue");
            tmt.up();
            tmt.goTo(newObstacle.get(i).getBottomLeftX(),newObstacle.get(i).getBottomLeftY());
            tmt.down();
            tmt.goTo(newObstacle.get(i).getBottomLeftX()+4,newObstacle.get(i).getBottomLeftY());
            tmt.goTo(newObstacle.get(i).getBottomLeftX()+4,newObstacle.get(i).getBottomLeftY()+4);
            tmt.goTo(newObstacle.get(i).getBottomLeftX(),newObstacle.get(i).getBottomLeftY()+4);
            tmt.goTo(newObstacle.get(i).getBottomLeftX(),newObstacle.get(i).getBottomLeftY());
            tmt.up();
            tmt.goTo(0, 0);
            tmt.speed(50);
        }
    }


    public void mazeBoader(){
        tmt.worldCoordinates(-100,-200,100,200);
        tmt.penColor("red");
        tmt.up();
        tmt.goTo(-100,200);
        tmt.down();

        for (int i = 0; i <2 ; i++) {
            tmt.forward(200);
            tmt.right(90);
            for (int j = 0; j < 1; j++) {
                tmt.forward(400);
                tmt.right(90);
            }
        }

        tmt.up();
        tmt.goTo(0,0);
        tmt.left(90);

    }

}