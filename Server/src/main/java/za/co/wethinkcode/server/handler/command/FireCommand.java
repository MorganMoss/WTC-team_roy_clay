package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.entity.Entity;
import za.co.wethinkcode.server.handler.world.entity.movable.Bullet;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

import java.awt.*;
import java.util.HashMap;

public class FireCommand extends Command {

    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {
        Point position = World.getRobot(robot).getPosition();
        int angle = World.getRobot(robot).getDirection().angle;
        Integer distance = 0;

        while (true){
            distance++;

            Entity hit = World.seek(position, angle, 1);

            if (hit == null){
                continue;
            }

            String result = hit.collidedWith(new Bullet(position));

            if (hit.getClass() == Robot.class){
                HashMap<String, Object> data = new HashMap<>();
                data.put("message", result);
                data.put("distance", distance);
                data.put("name", ((Robot) hit).getName());
                data.put("state", ((Robot) hit).getState());
                Response response = new Response("OK",data);
                return addRobotState(response);
            }

            if (result.equals("Obstructed")){
                Response response = new Response("OK", new HashMap<>(){{put("message", result);}});
                return addRobotState(response);
            }

            position = new Point(
                position.x + Math.toIntExact(Math.round(Math.sin(Math.toRadians(angle)))),
                position.y + Math.toIntExact(Math.round(Math.cos(Math.toRadians(angle)))));
        }
    }
}
