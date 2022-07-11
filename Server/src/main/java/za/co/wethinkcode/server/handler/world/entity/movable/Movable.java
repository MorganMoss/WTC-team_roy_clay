package za.co.wethinkcode.server.handler.world.entity.movable;

import org.jetbrains.annotations.Nullable;
import za.co.wethinkcode.server.handler.world.entity.Entity;

import java.awt.*;

/**
 * An entity with a mutable position
 */
public abstract class Movable implements Entity {

    protected Point position;
    public Direction direction;

    public enum Direction {
        NORTH (0),
        EAST (90),
        SOUTH (180),
        WEST (270);

        public final int angle;

        Direction(int degrees) {
            angle = degrees;
        }

        @Nullable
        private Direction getDirection(Direction west, Direction north, Direction east, Direction south) {
            switch (this){
                case NORTH:
                    return west;
                case EAST:
                    return north;
                case SOUTH:
                    return east;
                case WEST:
                    return south;
            }
            return null;
        }

        public Direction right(){
            return getDirection(EAST, SOUTH, WEST, NORTH);
        }
        public Direction left(){
            return getDirection(WEST, NORTH, EAST, SOUTH);
        }

        @Override
        public String toString() {
            switch (this){
                case NORTH:
                    return "NORTH";
                case EAST:
                    return "EAST";
                case SOUTH:
                    return "SOUTH";
                case WEST:
                    return "WEST";
            }
            return "";
        }
    }

    public Point getPosition() {
        return position;
    }


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