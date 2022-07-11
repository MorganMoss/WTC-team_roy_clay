package za.co.wethinkcode.server.handler.command;


import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

import java.awt.*;
import java.util.List;

import static java.lang.Math.min;
import static za.co.wethinkcode.server.Configuration.max_shield;
import static za.co.wethinkcode.server.Configuration.max_shots;

public class LaunchCommand extends Command {

    /**
     * Pre-formatted Response
     * for if there is no space in the world for a new robot
     */

    private static final Response NO_SPACE = Response.createError("No more space in this world");
    /**
     * Pre-formatted Response
     * for if the name given has already been used for another robot.
     */
    private static final Response NAME_TAKEN = Response.createError("Too many of you in this world");

    private String type;
    private int max_shield, max_shots;


    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {
        if (World.getRobot(robot) != null){
            return NAME_TAKEN;

        }

        Point initialPosition = World.getOpenPosition();
        if (initialPosition == null){
            return NO_SPACE;
        }
        
        World.addRobot(robot, new Robot(
                initialPosition,
                robot,
                null,
                min(max_shield, max_shield()),
                min(max_shots, max_shots())));

        Response response = Response.createOK();
        return addRobotState(response);
    }

    /**
     * Requires a String for type,
     * followed by Integers for maximum shield strength
     * and maximum shots
     * @param arguments of the command in question
     * @throws CouldNotParseArgumentsException if the arguments are invalid for this command
     */
    @Override
    public void setArguments(List<String> arguments) {
        if (arguments.size() != 3){
            throw new CouldNotParseArgumentsException();
        }

        try {
            type = arguments.get(0);
            max_shield = Integer.parseInt(arguments.get(1));
            max_shots = Integer.parseInt(arguments.get(2));
        } catch (NumberFormatException badArgument){
            throw new CouldNotParseArgumentsException();
        }
    }

    /**
     * Sets robot without caring about whether it exists or not
     * @param robot name of the robot being acted upon
     */
    @Override
    public void setRobot(String robot) {
        this.robot = robot;
    }
}
