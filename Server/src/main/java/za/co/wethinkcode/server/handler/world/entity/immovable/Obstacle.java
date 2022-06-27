package za.co.wethinkcode.server.handler.world.entity.immovable;

import za.co.wethinkcode.server.handler.world.entity.movable.Movable;

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
}

//    //TODO: Remove dead code
//    int size;
//
//    public Obstacle(Point position) {
//        this.position = position;
//        this.size=5;
//    }

//    @Override
//    public int getBottomLeftX() {
//        return x;
//    }
//
//    @Override
//    public int getBottomLeftY() {
//        return y;
//    }
//
//    @Override
//    public int getSize() {
//        return size;
//    }
//
//    @Override
//    public boolean blocksPosition(Position position) {
//        boolean checksX = this.x <= position.getX() && this.x +4 >= position.getX();
//        boolean checksY = this.y <= position.getY() && this.y +4 >= position.getY();
//
//        return checksX && checksY;
//    }
//
//    @Override
//    public boolean blocksPath(Position a, Position b) {
//        int xAxisMin =Math.min(a.getX(),b.getX());
//        int xAxisMax =Math.max(a.getX(),b.getX());
//        int yAxisMin =Math.min(a.getY(),b.getY());
//        int yAxisMax =Math.max(a.getY(),b.getY());
//
//        for (int x = xAxisMin; x < xAxisMax; x++) {
//
//            if(blocksPosition(new Position(x,a.getY()))){
//                return true;
//            }
//        }
//        for (int y = yAxisMin; y < yAxisMax; y++) {
//            if(blocksPosition(new Position(a.getX(),y))){
//                return true;
//            }
//        }
//        return false;
//    }
