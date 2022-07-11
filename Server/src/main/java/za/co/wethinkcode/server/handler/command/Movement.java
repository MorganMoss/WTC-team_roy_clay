package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.entity.Entity;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

import java.awt.*;
import java.util.List;

import static java.lang.Math.round;

public abstract class Movement extends Command {

    int steps;

    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {
        Robot robotEntity = World.getRobot(robot);
        Point currentPosition = robotEntity.getPosition();
        int directionAngle = robotEntity.getDirection().angle;
        String result = "Done";

        Entity foundEntity = World.Seek(currentPosition, directionAngle, steps);

        if (foundEntity == null){
            double radAngle = Math.toRadians(directionAngle);
            currentPosition.x += round(steps*Math.sin(radAngle));
            currentPosition.y += round(steps*Math.cos(radAngle));

            World.updatePosition(robot, currentPosition);
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
    }
}
