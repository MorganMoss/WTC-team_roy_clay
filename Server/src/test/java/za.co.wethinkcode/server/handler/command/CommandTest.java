package za.co.wethinkcode.server.handler.command;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.Configuration;
import za.co.wethinkcode.server.handler.world.World;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.min;
import static org.junit.jupiter.api.Assertions.*;
import static za.co.wethinkcode.server.Configuration.*;

class CommandTest {
    private Command command;
    private Response response;

    /**
     * Writes up a config to use during this test
     */
    @BeforeAll
    static void makeConfig(){
        String[] args = new String[2];
        args[0] = "-o=0,0,1,2";
        args[1] = "-s=3";
        setConfiguration(args);
    }

    /**
     * Resets the world before each test
     */
    @BeforeEach
    void resetWorld(){
        World.reset();
    }

    /**
     * Quick way to launch a robot
     */
    void launchRobot(String name, Integer shield, Integer shots){
        ArrayList<String> kinds = new ArrayList<>(){{
            add("sniper");
        }};
        //instantiating command
        command = new LaunchCommand();
        //initializing values
        command.setArguments(new ArrayList<>(){{
            add(kinds.get(0));
            add(String.valueOf(shield));
            add(String.valueOf(shots));
        }});
        command.setRobot(name);
        //running command
        response = command.execute();
    }

    /**
     * Checks the response to see if its a valid error
     */
    void assertBadArguments(){
        //fail to set arguments
        try {
            command.setArguments(new ArrayList<>(){{add("bad");add("data");add("here");}});
            fail("Did not validate arguments.");
        } catch (CouldNotParseArgumentsException ignored) {}
    }

    @Test
    void WorldCommandTest(){
        //instantiating command
        command = new WorldCommand();
        //initializing values
        command.setArguments(null);
        command.setRobot("ignored");
        //running command
        response = command.execute();

        assertNotNull(response);
        assertEquals("OK", response.getResult());
        assertEquals(
            new HashMap<String,String>(){{
            put("dimensions", "["+Configuration.size()+","+ Configuration.size()+"]");
            put("visibility", Configuration.visibility().toString());
            put("reload", Configuration.reload().toString());
            put("repair", Configuration.repair().toString());
            put("mine", Configuration.mine().toString());
            put("max-shields", max_shield().toString());
            put("max-shots", max_shots().toString());
            }},
            response.getData()
        );
        assertNull(response.getState());
    }
    @Test
    void worldCommandBadArgs(){
        //instantiating command
        command = new WorldCommand();
        //initializing values
        command.setRobot("ignored");
        //check if bad args response
        assertBadArguments();
    }

    @Test
    void LaunchCommandTest(){
        Integer shield = 5, shots = 5;

        launchRobot("HAL", shield, shots);

        assertNotNull(response);
        assertEquals("OK", response.getResult());
        assertEquals(new HashMap<String,String>(), response.getData());

        //TODO:
        // This should not necessarily be 0,0.
        // It should be pulled from the World.
        int x = 0, y = 0;

        assertEquals(
            new HashMap<String, String>(){{
               put("position", "["+World.getRobot("HAL").getPosition().x+","+World.getRobot("HAL").getPosition().y+"]");
               put("direction", "NORTH");
               put("shields", ((Integer)  min(shield, max_shield())).toString());
               put("shots", ((Integer) min(shots, max_shots())).toString());
               put("status", "NORMAL");
            }},
            response.getState()
        );
    }

    //TODO:
    // - No Free Location
    // - Name Already Taken

    @Test
    void launchCommandBadArgs(){
        //instantiating command
        command = new WorldCommand();
        //initializing values - bad args
        command.setRobot("HAL");
        //check if bad args response
        assertBadArguments();
    }

    @Test
    void stateCommandTest(){
        Integer shield = 5, shots = 5;

        launchRobot("HAL", shield, shots);

        command = new StateCommand();
        command.setArguments(null);
        command.setRobot("HAL");
        response = command.execute();

        int x = 0, y = 0;

        assertEquals(
            new HashMap<String, String>(){{
                put("position", "["+World.getRobot("HAL").getPosition().x+","+World.getRobot("HAL").getPosition().y+"]");
                put("direction", "NORTH");
                put("shields", ((Integer)  min(shield, max_shield())).toString());
                put("shots", ((Integer) min(shots, max_shots())).toString());
                put("status", "NORMAL");
            }},
            response.getState()
        );
    }

    //TODO:
    // - All the other command success and fail cases
    // - Look
    //      - Successful
    // - Movement
    //      - Successful
    //      - Obstructed (by obstacle)
    //      - Obstructed (by robot)
    //      - Fell
    //      - Mine
    // - Turn
    //      - Successful
    // - Repair
    //      - Successful
    // - Reload
    //      - Successful
    // - Mine
    //      - Successful
    // - Fire
    //      - Hit
    //      - Miss

    static class BadDataType {

    }
}
