package za.co.wethinkcode.server.handler.command;


import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.Handler;

import java.util.HashMap;
import java.util.List;

//Out of spec
//TODO: Consider removal.
public class LaunchCommand extends Command {
    /**
     * Pre-formatted Response
     * for if there is no space in the world for a new robot
     */
    private static final Response NO_SPACE = Response.createOK("No more space in this world");
    /**
     * Pre-formatted Response
     * for if the name given has already been used for another robot.
     */
    private static final Response NAME_TAKEN = Response.createOK("Too many of you in this world");

    private String type;
    private int maximumShieldStrength, maximumShots;


    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {
        //TODO
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
    public void setArguments(List<?> arguments) {
        if (arguments.size() != 3){
            throw new CouldNotParseArgumentsException();
        }

        try {
            type = (String) arguments.get(0);
            maximumShieldStrength = (int) arguments.get(1);
            maximumShots = (int) arguments.get(2);
        } catch (ClassCastException badArgument){
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
