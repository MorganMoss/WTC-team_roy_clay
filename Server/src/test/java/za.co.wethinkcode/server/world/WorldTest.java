package za.co.wethinkcode.server.world;


import org.junit.jupiter.api.Test;
import za.co.wethinkcode.server.configuration.Configuration;
import za.co.wethinkcode.server.TestHelper;
import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

import java.awt.*;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {
    @Test
    void worldAutomaticallyInstantiated() {
        //Should not require reset() to be called, unless a change in config occurred.
        Configuration.setConfiguration(new String[]{});
        assertNotNull(World.getInstance());
    }

    @Test
    void AddingRobotThenRemovingRobotSuccessful() {
        TestHelper.modifyWorld(new String[]{"-s=1"});

        assertEquals(new Point(0, 0), World.getOpenPosition());

        Robot robot = new Robot(new Point(0, 0), "HAL", null, 1, 1);
        World.addRobot("HAL", robot);

        assertEquals(new HashSet<String>() {{
            add("HAL");
        }}, World.getRobots());
        assertEquals(robot, World.getRobot("HAL"));
        assertNull(World.getOpenPosition());

        World.removeRobot("HAL");

        assertNull(World.getRobot("HAL"));
        assertEquals(new Point(0, 0), World.getOpenPosition());
    }

    @Test
    void predefinedObstaclesAddedCorrectly() {
        TestHelper.modifyWorld(new String[]{"-s=2", "-o=0,0,1,1,0,1,-1,-1,0,-1,-1,1,1,-1,-1,0"});

        //only one space should be left at 1,0 after adding 3 obstacles
        //should be noted that
        for (int i = 0; i < 4; i++) {
            assertEquals(new Point(1, 0), World.getOpenPosition());
        }
    }

    @Test
    void resettingWorldSuccessfully() {
        //checking via open positions
        TestHelper.modifyWorld(new String[]{"-s=1"});

        for (int i = 0; i < 4; i++) {
            assertEquals(new Point(0, 0), World.getOpenPosition());
        }

        TestHelper.modifyWorld(new String[]{"-s=2", "-o=0,0,1,1,0,1,-1,-1,0,-1,-1,1,1,-1,-1,0"});

        for (int i = 0; i < 4; i++) {
            assertEquals(new Point(1, 0), World.getOpenPosition());
        }

        TestHelper.modifyWorld(new String[]{});

        for (int i = 0; i < 4; i++) {
            assertEquals(new Point(0, 0), World.getOpenPosition());
        }
    }

    //TODO:
    // Test bad config errors (i.e bad predefined entities and size)

    //TODO:
    // Test adding Entity with a valid open position
    // Test adding Entity out of bounds
    // Test adding Entity to occupied space
    // Test removal of Entity

    //TODO:
    // Test seek from position method (i.e. the general method for the below)
    // Test the look method
    // Test path blocked method
    // Test fire method

    //TODO:
    // Test intersection of a robot and another entity in general
    // (e.g. when a robot lands in a pit, the pit should remain)

    //TODO:
    // Test the saving of a world instance
    // Test the loading of a world instance

    //TODO:
    // Test addition of mines
    // Test detonation of mines


}