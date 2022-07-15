package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.world.World;

import java.util.List;

public class TurnCommand extends Command {

    /**
    True if left, False if right
     */
    private boolean direction;


    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {
        //TODO: Should make an acceptance test for this,
        // then implement it correctly
        if (direction){
            World.getRobot(robot).setDirection(World.getRobot(robot).getDirection().left());
        } else {
            World.getRobot(robot).setDirection(World.getRobot(robot).getDirection().right());
        }

        Response response = Response.createOK("Done");
        return addRobotState(response);
    }

    /**
     * Requires a single argument, "left" or "right"
     * @param arguments of the command in question
     * @throws CouldNotParseArgumentsException if the arguments are invalid for this command
     */
    @Override
    public void setArguments(List<String> arguments) {
        if (arguments.size() != 1){
            throw new CouldNotParseArgumentsException();
        }
        switch (arguments.get(0).toLowerCase()){
            case "left":
                direction = true;
                break;
            case "right":
                direction = false;
                break;
            default:
                throw new CouldNotParseArgumentsException();
        }
    }
}
