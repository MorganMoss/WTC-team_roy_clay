package za.co.wethinkcode.server.handler.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.Configuration;
import za.co.wethinkcode.server.TestHelper;
import za.co.wethinkcode.server.handler.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.Math.min;
import static org.junit.jupiter.api.Assertions.*;
import static za.co.wethinkcode.server.Configuration.*;

class CommandTest {
    private Command command;
    private Response response;

    /**
     * Resets the world before each test
     */
    @BeforeEach
//    void resetWorld(){
//        TestHelper.modifyWorld(new String[]{"-o=0,0,1,2","-s=3"});
//    }
    void resetWorld(){
        TestHelper.modifyWorld(new String[]{"-o=0,1,0,9","-s=20"});
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
        command.setArguments(new ArrayList<>());
        command.setRobot("ignored");
        //running command
        response = command.execute();

        assertNotNull(response);
        assertEquals("OK", response.getResult());



        HashMap<String, ?> correct = new HashMap<>(){{
            put("visibility", Configuration.visibility());
            put("max-shields", Configuration.max_shield());
            put("max-shots", Configuration.max_shots());
            put("reload", Configuration.reload());
            put("repair", Configuration.repair());
            put("mine", Configuration.mine());
        }};

        assertEquals(Configuration.size(), ((int[]) response.getData().get("dimensions"))[0]);
        assertEquals(Configuration.size(), ((int[]) response.getData().get("dimensions"))[1]);

        for (String key: correct.keySet()){
            assertEquals(correct.get(key), response.getData().get(key), key);
        }

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

        assertEquals(World.getRobot("HAL").getPosition().x, ((int[]) response.getState().get("position"))[0]);
        assertEquals(World.getRobot("HAL").getPosition().y, ((int[]) response.getState().get("position"))[1]);

        HashMap<String, ?> correct = new HashMap<>(){{
            put("direction", "NORTH");
            put("shields", min(shield, max_shield()));
            put("shots", min(shots, max_shots()));
            put("status", "NORMAL");
        }};

        for (String key: correct.keySet()){
           assertEquals(correct.get(key), response.getState().get(key));
        }
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
        command.setArguments(new ArrayList<>());
        command.setRobot("HAL");
        response = command.execute();

        int x = 0, y = 0;

        assertEquals(World.getRobot("HAL").getPosition().x, ((int[]) response.getState().get("position"))[0]);
        assertEquals(World.getRobot("HAL").getPosition().y, ((int[]) response.getState().get("position"))[1]);

        HashMap<String, ?> correct = new HashMap<>(){{
            put("direction", "NORTH");
            put("shields", min(shield, max_shield()));
            put("shots", min(shots, max_shots()));
            put("status", "NORMAL");
        }};

        for (String key: correct.keySet()){
            assertEquals(correct.get(key), response.getState().get(key));
        }
    }

    private void look(String name) {

        command = new LookCommand();
        command.setRobot(name);
        response = command.execute();
    }
    @Test
    void LookCommandTest(){

        Integer shield = 5, shots = 5;
        launchRobot("HAL", shield, shots);
        look("HAL");

//        assertNotNull(response);
//        assertEquals("OK", response.getResult());
//        assertEquals(new HashMap<String,String>(), response.getData());
//
//        //TODO:
//        // This should not necessarily be 0,0.
//        // It should be pulled from the World.
//        int x = 0, y = 0;
//
//        assertEquals(World.getRobot("HAL").getPosition().x, ((int[]) response.getState().get("position"))[0]);
//        assertEquals(World.getRobot("HAL").getPosition().y, ((int[]) response.getState().get("position"))[1]);
//
//        HashMap<String, ?> correct = new HashMap<>(){{
//            put("direction", "NORTH");
//            put("shields", min(shield, max_shield()));
//            put("shots", min(shots, max_shots()));
//            put("status", "NORMAL");
//        }};
//
//        for (String key: correct.keySet()){
//            assertEquals(correct.get(key), response.getState().get(key));
//        }
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
