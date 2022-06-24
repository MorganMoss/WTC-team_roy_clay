package za.co.wethinkcode.server.handler.world;

import kotlin.Pair;
import za.co.wethinkcode.server.handler.world.entity.Entity;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

import static za.co.wethinkcode.server.Configuration.*;

import java.awt.*;
import java.util.Hashtable;
import java.util.List;

/**
 * The world is a container that manages Entities,
 * Has a fixed 2 dimensional size that contains all the Entities.
 *
 */
public class World {

    private static volatile World instance = null;

    private final Hashtable<String, Robot> robots = new Hashtable<>();
    private final Hashtable<Point, Entity> entityTable = new Hashtable<>();

    private World(){
        //Use configuration values here to dictate properties of the world.
        for (String entity: List.of(obstacle(), pits(), mines())) {
            if (entity.equalsIgnoreCase("none")
            | !entity.matches("[[0-9]+,[0-9]+,?]+")){
                continue;
            }

//            for (String number : entity.split(",");

        }
    }

    public static World getInstance(){
        if(instance == null){
            synchronized (World.class){
                if(instance == null){
                    instance = new World();
                }
            }
        }
        return instance;
    }

    /**
     * Resets world to initial values
     */
    public static void reset() {
        //TODO
    }
}

