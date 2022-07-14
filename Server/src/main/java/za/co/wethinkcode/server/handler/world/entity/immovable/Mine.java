package za.co.wethinkcode.server.handler.world.entity.immovable;

import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.entity.movable.Movable;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

import java.awt.*;

/**
 * A mine is a temporary, but stationary entity. It blows up and disappears.
 */
public class Mine extends Immovable {
    /**
     * An immovable entity is instantiated with its final position
     * @param position of this entity
     */
    public Mine(Point position) {
        super(position);
    }

    /**
     * A robot that walks over this will lose shield, and this mine will be removed from the world
     * @param entity The entity moving into the same space as this entity.
     * @return "Mine"
     */
    @Override
    public String collidedWith(Movable entity) {

        //Remove this mine from the world
        World.removeEntity(position);

        if (entity.getClass().equals(Robot.class)){
            String robot = ((Robot) entity).getName();
            //TODO: Remove 3 shield

            World.updatePosition(robot, position);
        }

        return "Mine";
    }

    @Override
    public String toString() {
        return "MINE";
    }
}
