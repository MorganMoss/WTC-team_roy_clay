package za.co.wethinkcode.server.handler.world.map;

import za.co.wethinkcode.server.handler.world.entity.immovable.Immovable;
import za.co.wethinkcode.server.handler.world.entity.immovable.Obstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RandomMap implements Map {
    List immovable ;
    Random random;


    public RandomMap(){
        this.random=new Random();
        this.immovable = new ArrayList();
        createRandomImmovables();
    }


    @Override
    public List<Immovable> getImmovables() {
        return this.immovable;
    }


    public List<Immovable> createRandomImmovables(){
        int randomNumber= random.nextInt(10);
        for (int i = 0; i < randomNumber; i++) {
            int immovableX =random.nextInt(201)-100;
            int immovableY=random.nextInt(401)-200;
        this.immovable.add(new Obstacle(immovableX,immovableY));
    }return this.immovable;
    }

}
