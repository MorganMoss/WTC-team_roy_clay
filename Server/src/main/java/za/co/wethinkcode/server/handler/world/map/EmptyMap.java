package za.co.wethinkcode.server.handler.world.map;

import za.co.wethinkcode.server.handler.world.entity.immovable.Immovable;

import java.util.ArrayList;
import java.util.List;

public class EmptyMap implements Map {

    List immovables ;
    public EmptyMap(){
      this.immovables=new ArrayList<>();
    }

    @Override
    public List<Immovable> getImmovables() {
        return this.immovables;
    }

}
