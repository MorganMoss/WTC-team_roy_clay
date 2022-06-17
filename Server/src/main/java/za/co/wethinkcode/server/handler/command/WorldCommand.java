package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;

public class WorldCommand extends Command {

    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {
        //TODO: Should make an acceptance test for this,
        // then implement it correctly
        return null;
    }

    /**
     * Ignored by this command
     * @param robot name of the robot being acted upon
     */
    @Override
    public void setRobot(String robot) {}
}
