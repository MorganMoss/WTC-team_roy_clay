package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.entity.Entity;
import za.co.wethinkcode.server.handler.world.entity.movable.Movable;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

import java.awt.*;
import java.util.List;

import static java.lang.Math.round;

public abstract class Movement extends Command {

   private int steps;
   private Robot robotEntity;
   private Point currentPosition;

   protected Movable.Direction direction;
   private void move(){
       double radAngle = Math.toRadians(direction.angle);
       currentPosition.x += round(steps*Math.sin(radAngle));
       currentPosition.y += round(steps*Math.cos(radAngle));
       World.updatePosition(robot, currentPosition);
   }

    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {
        Entity foundEntity = World.Seek(currentPosition, direction.angle, steps);

        String result;

        if (foundEntity == null){
            move();
            result = "Done";
        } else {
            result = foundEntity.collidedWith(robotEntity);
        }

        Response response = Response.createOK(result);
        return addRobotState(response);
    }

    /**
     * Requires an integer that represents the steps the robot takes
     * @param arguments of the command in question
     * @throws CouldNotParseArgumentsException if the arguments are invalid for this command
     */
    @Override
    public void setArguments(List<String> arguments) {
        if (arguments.size() != 1){
            throw new CouldNotParseArgumentsException();
        }

        try {
            steps = Integer.parseInt(arguments.get(0));
        } catch (NumberFormatException badArgument){
            throw new CouldNotParseArgumentsException();
        }

        if (steps <= 0) {
            throw new CouldNotParseArgumentsException();
        }
    }

    @Override
    public void setRobot(String robot) {
        super.setRobot(robot);
        direction = World.getRobot(robot).getDirection();
        robotEntity = World.getRobot(robot);
        currentPosition = robotEntity.getPosition();
    }
}
