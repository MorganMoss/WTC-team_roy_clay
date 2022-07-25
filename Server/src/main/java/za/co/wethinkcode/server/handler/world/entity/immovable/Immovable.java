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


