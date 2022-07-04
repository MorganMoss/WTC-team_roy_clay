package za.co.wethinkcode.server;

import za.co.wethinkcode.server.handler.world.World;

import static za.co.wethinkcode.server.Configuration.setConfiguration;

public class TestHelper {
    /**
     * Changes the configuration and resets the world
     * @param config the arguments to be parsed as configuration.
     */
    public static void modifyWorld(String[] config){
        setConfiguration(config);
        World.reset();
    }
}
