package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;

public class MineCommand extends Command {

    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {
        //TODO
        Response response = Response.createOK("Done");
        return addRobotState(response);
    }
}
