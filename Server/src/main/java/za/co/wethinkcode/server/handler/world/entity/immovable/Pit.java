package za.co.wethinkcode.server.handler.world.entity.immovable;

import za.co.wethinkcode.server.handler.world.entity.movable.Movable;

import java.awt.*;

/**
 * A pit is a stationary entity a robot can fall into
 */
public class Pit extends Immovable {
    /**
     * An immovable entity is instantiated with its final position
     * @param position of this entity
     */
    public Pit(Point position) {
        super(position);
    }

    /**
     * Walking into a pit will instantly kill a robot
     * @param entity The entity moving into the same space as this entity.
     * @return "Fell"
     */
    @Override
    public String collidedWith(Movable entity) {
        //TODO: Kill the movable entity

        return "Fell";
    }

    @Override
    public String toString() {
        return "PIT";
    }
}
