package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;

public class StateCommand extends Command {

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
}
