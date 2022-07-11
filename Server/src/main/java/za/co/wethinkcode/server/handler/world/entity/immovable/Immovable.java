package za.co.wethinkcode.server.handler.world.entity.immovable;

import za.co.wethinkcode.server.handler.world.entity.Entity;

import java.awt.*;

/**
 * An entity with an immutable position.
 */
public abstract class Immovable implements Entity {
    protected final Point position;

    /**
     * An immovable entity is instantiated with its final position
     * @param position of this entity
     */
    protected Immovable(Point position){
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }
}

//    /**
//     * Get X coordinate of bottom left corner of obstacle.
//     * @return x coordinate
//     */
//    int getBottomLeftX();
//
//    /**
//     * Get Y coordinate of bottom left corner of obstacle.
//     * @return y coordinate
//     */
//    int getBottomLeftY();
//
//    /**
//     * Gets the side of an obstacle (assuming square obstacles)
//     * @return the length of one side in nr of steps
//     */
//    int getSize();

