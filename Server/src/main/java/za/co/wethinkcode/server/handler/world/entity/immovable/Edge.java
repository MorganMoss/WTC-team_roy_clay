package za.co.wethinkcode.server.handler.world.entity.immovable;

import za.co.wethinkcode.server.handler.world.entity.movable.Movable;

import java.awt.*;

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

    /**
     * An obstacle obstructs the path,
     * It forces a robot to move back to one position before this entity
     * @param entity The entity moving into the same space as this entity.
     * @return "Obstructed"
     */
    @Override
    public String collidedWith(Movable entity) {
        //entity should be moved to the closest empty block to their previous position.
        return "Obstructed";
    }

    @Override
    public String toString() {
        return "EDGE";
    }
}