package za.co.wethinkcode.server.handler.world.entity.movable;

import za.co.wethinkcode.server.handler.world.entity.Entity;

import java.awt.*;

public abstract class Movable implements Entity {
    protected Point position;

    /**
     * Change the position of the entity
     * @param newPosition the new position of this entity
     */
    void updatePosition(Point newPosition){
        position = newPosition;
    }
}