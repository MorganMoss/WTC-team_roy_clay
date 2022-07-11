package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;

import java.util.HashMap;

import za.co.wethinkcode.server.configuration.Configuration;

import static za.co.wethinkcode.server.configuration.Configuration.*;

public class WorldCommand extends Command {

    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {
        return Response.createOK(new HashMap<>(){{
            put("dimensions", new int[]{Configuration.size(), Configuration.size()});
            put("visibility", visibility());
            put("max-shots", max_shots());
            put("max-shields", max_shield());
            put("repair", repair());
            put("reload", reload());
            put("mine", mine());
        }});
    }

    /**
     * Ignored by this command
     * @param robot name of the robot being acted upon
     */
    @Override
    public void setRobot(String robot) {}
}
