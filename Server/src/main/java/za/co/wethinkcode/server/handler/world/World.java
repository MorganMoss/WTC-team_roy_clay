package za.co.wethinkcode.server.handler.world;

import za.co.wethinkcode.server.handler.world.map.Map;

public class World extends AbstractWorld{

    //TODO: Move blocks position and path responsibility to the world.
    public World(Map maze) {
        super(maze);
    }
}
