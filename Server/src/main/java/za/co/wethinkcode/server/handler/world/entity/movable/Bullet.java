package za.co.wethinkcode.server.handler.world.entity.movable;

import java.awt.*;

public class Bullet extends Movable{
    /**
     * A movable entity is instantiated with its starting position
     *
     * @param position of this entity initially
     */
    public Bullet(Point position) {
        super(position);
    }

    @Override
    public String collidedWith(Movable entity) {
        return "Hit";
    }
}
