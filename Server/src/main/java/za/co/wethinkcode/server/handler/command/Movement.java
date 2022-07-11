package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.entity.Entity;
import za.co.wethinkcode.server.handler.world.entity.movable.Movable;

import java.awt.*;
import java.sql.SQLOutput;
import java.util.List;

public abstract class Movement extends Command {

    public int steps;

    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {

        //get robot's current direction & corresponding angle
        Movable.Direction currentDirection = World.getRobot(robot).direction;
        double directionAngle = currentDirection.angle;

        //returns the position of the robot when look command is called
        Point currentPosition = World.getRobot(robot).getPosition();

        for (int step = 1; step <= Math.abs(steps); step++){

            Entity foundEntity = World.Seek(currentPosition, directionAngle, step);

            if (foundEntity != null) {
                //return a response based on the entity that was encountered and the state of the robot after the entity is encountered.
            }
            else if (foundEntity == null){
                //move the robot forward or back by one step in the world.
                System.out.println("moved one step forward or back in the world");
            }
        }
        //return a response with result = OK, data > message = Done, state updated with new position
        System.out.println("Hal moved forward or back 0 < = x <= n steps ");
        Response response = Response.createOK("Done");
        return addRobotState(response);


        //TODO
//        int nrSteps = Integer.parseInt(getArgument());
//        if (target.getWorld().updatePosition(-nrSteps).equals(World.UpdateResponse.SUCCESS)){
//            target.setStatus("Moved back by "+nrSteps+" steps.");
//        }else if (target.getWorld().updatePosition(-nrSteps).equals(World.UpdateResponse.FAILED_OUTSIDE_WORLD)) {
//            target.setStatus("Sorry, I cannot go outside my safe zone.");
//        }else if (target.getWorld().updatePosition(-nrSteps).equals(World.UpdateResponse.FAILED_OBSTRUCTED)) {
//            target.setStatus("Sorry, there is an obstacle in the way.");}
//        return true;
//        Response response = Response.createOK("Done");
//        return addRobotState(response);
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
