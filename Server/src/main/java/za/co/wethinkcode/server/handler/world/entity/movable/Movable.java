package za.co.wethinkcode.server.handler.world.entity.movable;

import za.co.wethinkcode.server.handler.world.entity.Entity;

import java.awt.*;

/**
 * An entity with a mutable position
 */
public abstract class Movable implements Entity {
    protected Point position;

    /**
     * A movable entity is instantiated with its starting position
     * @param position of this entity initially
     */
    protected Movable(Point position){
        this.position = position;
    }

    /**
     * Change the position of the entity
     * @param newPosition the new position of this entity
     */
    void updatePosition(Point newPosition){
        position = newPosition;
    }
}