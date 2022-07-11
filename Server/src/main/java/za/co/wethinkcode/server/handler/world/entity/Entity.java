package za.co.wethinkcode.server.handler.world.entity;

import za.co.wethinkcode.server.handler.world.entity.movable.Movable;

import java.awt.*;

public interface Entity {
    /**
     * Called when an entity is collided with,
     * Intended for use against movable robots.
     * This can effect the resultant position of a movable
     * and can effect the state of a robot or other movable.
     * @param entity The entity moving into the same space as this entity.
     * @return The result of that collision,
     * will be put into the message value in the data of the Response
     */
    String collidedWith(Movable entity);

    Point getPosition();
}
