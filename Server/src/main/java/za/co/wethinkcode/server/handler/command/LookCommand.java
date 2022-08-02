package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.configuration.Configuration;
import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.entity.Entity;
import za.co.wethinkcode.server.handler.world.entity.movable.Movable;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.round;

//TODO: Implement correct functionality.
public class LookCommand extends Command {

    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {
        HashMap<String, ArrayList<HashMap<String, ?>>> dataMap = new HashMap<>();
        ArrayList<HashMap<String, ?>> objects = new ArrayList<>();

        //returns how many steps ahead the robot can see
        int visibility = Configuration.visibility();

        //returns the position of the robot when look command is called
        Point currentPosition = World.getRobot(robot).getPosition();

        //List of directions that the robot needs to look in
        Movable.Direction[] directions = new Movable.Direction[]{
                Movable.Direction.NORTH,
                Movable.Direction.EAST,
                Movable.Direction.SOUTH,
                Movable.Direction.WEST
        };

        //robot looks in each direction within visible range and returns the first entity it sees in that direction
        for (Movable.Direction direction : directions) {

            int directionAngle = direction.angle;
            Entity foundEntity = World.seek(currentPosition, directionAngle, visibility);
            System.out.println(foundEntity);

            if (foundEntity != null) {
                Integer distance = Math.toIntExact(round(currentPosition.distance(foundEntity.getPosition())));
                HashMap<String, Object> object = new HashMap<>();
                object.put("direction", direction.toString());
                object.put("type", foundEntity.toString()); //"OBJECT@qjsjbhd123213" -> Obstacle etc
                object.put("distance", distance);
                objects.add(object);
            }
        }

        dataMap.put("objects" , objects);
        Response response = Response.createOK(dataMap);
        System.out.println(dataMap);
        return addRobotState(response);
    }
}
