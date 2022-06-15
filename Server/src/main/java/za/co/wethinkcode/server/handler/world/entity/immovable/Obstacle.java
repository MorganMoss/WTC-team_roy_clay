package za.co.wethinkcode.server.handler.world.entity.immovable;

import za.co.wethinkcode.server.handler.world.Position;


//We need to remove hard-coded values from here.
//i.e. TODO: Refactor to allow dynamic size.
public class Obstacle implements Immovable {
    int x;
    int y;
    int size;


    public Obstacle(int x, int y) {
        this.x=x;
        this.y=y;
        this.size=5;
    }

    @Override
    public int getBottomLeftX() {
        return x;
    }

    @Override
    public int getBottomLeftY() {
        return y;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean blocksPosition(Position position) {
        boolean checksX = this.x <= position.getX() && this.x +4 >= position.getX();
        boolean checksY = this.y <= position.getY() && this.y +4 >= position.getY();

        return checksX && checksY;
    }

    @Override
    public boolean blocksPath(Position a, Position b) {
        int xAxisMin =Math.min(a.getX(),b.getX());
        int xAxisMax =Math.max(a.getX(),b.getX());
        int yAxisMin =Math.min(a.getY(),b.getY());
        int yAxisMax =Math.max(a.getY(),b.getY());

        for (int x = xAxisMin; x < xAxisMax; x++) {

            if(blocksPosition(new Position(x,a.getY()))){
                return true;
            }
        }
        for (int y = yAxisMin; y < yAxisMax; y++) {
            if(blocksPosition(new Position(a.getX(),y))){
                return true;
            }
        }
        return false;
    }
}