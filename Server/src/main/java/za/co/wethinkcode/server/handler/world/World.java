package za.co.wethinkcode.server.handler.world;

import static za.co.wethinkcode.server.Configuration.*;
import java.util.concurrent.Callable;

/**
 * The world is a container that manages Entities,
 * Has a fixed 2 dimensional size that contains all the Entities.
 *
 */
public class World {

    private static volatile World instance = null;

    private World(){
        //Use configuration values here to dictate properties of the world.
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
}
