package za.co.wethinkcode.server.world.map;

import za.co.wethinkcode.server.world.entity.immovable.Immovable;
import za.co.wethinkcode.server.world.entity.immovable.Obstacle;

import java.util.ArrayList;
import java.util.List;

public class SimpleMap implements Map {

    @Override
    public List<Immovable> getImmovables() {
        List immovable =new ArrayList();
        immovable.add(new Obstacle(1,1));
        return immovable;
    }

}
