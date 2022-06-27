package za.co.wethinkcode.server.handler.command;

import java.util.List;

import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.world.World;

public abstract class Command {
    protected String robot;

    /**
     * Adds the robot of given name's state to a response.
     * @param response needing the state
     * @return response updated to contain the state
     */
    protected Response addRobotState(Response response){
        //TODO: ADD STATE
        response.addState(World.getRobot(robot).getState());
        return response;
    }

    /**
     * Validates and sets the arguments for the command.
     * Ignored by commands by default
     * @param arguments of the command in question
     * @throws CouldNotParseArgumentsException If any arguments are present
     */
    public void setArguments(List<String> arguments) {
        if (arguments != null){
            throw new CouldNotParseArgumentsException();
        }
    }

    /**
     * Validates and sets the name of the robot for the command by default
     * @param robot name of the robot being acted upon
     * @throws RobotNotFoundException if the robot does not exist
     */
    public void setRobot(String robot){
        this.robot = robot;
        //TODO: VALIDATE
    }

    /**
     * Abstract method to be implemented by child classes.
     * This will modify, add, remove and view aspects of the world package.
     * @return a response formulated off the results of the execution.
     */
    public abstract Response execute();

}
