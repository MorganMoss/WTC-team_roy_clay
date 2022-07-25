package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.entity.immovable.Mine;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

import java.awt.*;
import java.util.ArrayList;

public class MineCommand extends Command {

    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {
        Robot robotEntity =  World.getRobot(robot);
        Point position = robotEntity.getPosition();

        ForwardCommand forwardCommand = new ForwardCommand();
        forwardCommand.setRobot(robot);
        forwardCommand.setArguments(new ArrayList<>(){{add("1");}});

        String result = (String) forwardCommand.execute().getData().get("message");

        if (!robotEntity.getPosition().equals(position)){
            World.addEntity(position, new Mine(position));
        } else {
            result = new Mine(position).collidedWith(robotEntity);
        }

        Response response = Response.createOK(result);
        return addRobotState(response);
    }
}
