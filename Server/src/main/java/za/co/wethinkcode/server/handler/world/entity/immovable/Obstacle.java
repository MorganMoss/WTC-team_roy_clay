package za.co.wethinkcode.server.handler.world.entity.immovable;

import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.entity.movable.Movable;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

import java.awt.*;

/**
 * An obstacle is a stationary entity that blocks the path of any movable entity
 */
public class Obstacle extends Immovable {
    /**
     * An immovable entity is instantiated with its final position
     * @param position of this entity
     */
    public Obstacle(Point position) {
        super(position);
    }

    private static int getDelta(int a, int b){
        int c = b - a;

        if (c == 0) {
            return c;
        }

        c += c < 0 ? 1 : -1;

        return c;
    }

    private Point getOnePositionBefore(Point position){
        position.y += getDelta(position.y, this.position.y);
        position.x += getDelta(position.x, this.position.x);
        return position;
    }

    /**
     * An obstacle obstructs the path,
     * By default, it forces a robot to move back to one position before this entity
     * @param entity The entity moving into the same space as this entity.
     * @return "Obstructed"
     */
    @Override
    public String collidedWith(Movable entity) {
        //entity should be moved to the closest empty block to their previous position.
        if (entity.getClass().equals(za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot.class)){
            World.updatePosition(
                    ((Robot) entity).getName(),
                    getOnePositionBefore(entity.getPosition())
            );
        }

        return "Obstructed";
    }

    @Override
    public String toString() {
        return "OBSTACLE";
    }
}