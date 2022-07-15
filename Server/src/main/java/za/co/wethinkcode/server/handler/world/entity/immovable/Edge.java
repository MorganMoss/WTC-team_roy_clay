package za.co.wethinkcode.server.handler.world.entity.immovable;

import za.co.wethinkcode.server.handler.world.entity.movable.Movable;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

import java.awt.Point;

import static za.co.wethinkcode.server.configuration.Configuration.size;

/**
 * An obstacle is a stationary entity that blocks the path of any movable entity
 */
public class Edge extends Immovable {
    /**
     * An immovable entity is instantiated with its final position
     * @param position of this entity
     */
    public Edge(Point position) {
        super(position);
    }

    @Override
    public String collidedWith(Movable entity){
        String message = "At the ? edge";

        if (Robot.class.equals(entity.getClass())){

            new Obstacle(position).collidedWith(entity);

            if (position.y > size()/2){
                message = "At the NORTH edge";
            }
            if (position.y < - size()/2){
                message = "At the SOUTH edge";
            }
            if (position.x > size()/2) {
                message = "At the EAST edge";
            }
            if (position.x < - size()/2){
                message = "At the WEST edge";
            }
        }

        return message;
    }

    @Override
    public String toString() {
        return "EDGE";
    }
}