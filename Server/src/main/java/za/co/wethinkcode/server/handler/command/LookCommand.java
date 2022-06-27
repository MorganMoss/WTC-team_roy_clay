package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;

import java.util.HashMap;

//TODO: Implement correct functionality.
public class LookCommand extends Command {

    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {
        //TODO
        Response response = Response.createOK(new HashMap<>());
        return addRobotState(response);
    }
}
