package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.Configuration;
import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.entity.Entity;
import za.co.wethinkcode.server.handler.world.entity.movable.Movable;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

import javax.sound.midi.Soundbank;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


//TODO: Implement correct functionality.
public class LookCommand extends Command {

    /**
     * TODO
     * @return
     */

    @Override
    public Response execute() {
        //TODO

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

            double directionAngle = direction.angle;
            Entity foundEntity = World.Seek(currentPosition, directionAngle, visibility);

            //add information about the found entity to the response object
        }

        Response response = Response.createOK(new HashMap<>());
        System.out.println(response.getResult());
        return addRobotState(response);
    }
}
