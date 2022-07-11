package za.co.wethinkcode.server.handler.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.Configuration;
import za.co.wethinkcode.server.TestHelper;
import za.co.wethinkcode.server.handler.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.min;
import static org.junit.jupiter.api.Assertions.*;
import static za.co.wethinkcode.server.Configuration.max_shield;
import static za.co.wethinkcode.server.Configuration.max_shots;

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
        TestHelper.modifyWorld(new String[]{"-o=0,1","-s=20"});
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
    void LookCommandTestSeeAll4Edges() {
        TestHelper.modifyWorld(new String[]{"-s=18"});

        Integer shield = 5, shots = 5;
        launchRobot("HAL", shield, shots);
        look("HAL");

    }

    @Test
    void LookCommandTestSeeNoEdges() {
        TestHelper.modifyWorld(new String[]{"-s=25"});
        Integer shield = 5, shots = 5;
        launchRobot("HAL", shield, shots);
        look("HAL");

    }

    @Test
    void LookCommandTestSeeObstacle() {
        TestHelper.modifyWorld(new String[]{"-o=12,13,12,11,11,12,13,12","-s=25"});

        Integer shield = 5, shots = 5;
        launchRobot("HAL", shield, shots);
        look("HAL");
    }


    @Test
    void ForwardCommandNoObstructsTest() {

        //Given default visibility = 5 & world size > visibility
        TestHelper.modifyWorld(new String[]{"-o=none","-s=20"});
        launchRobot("HAL", 5, 5);

        Point position = World.getRobot("HAL").getPosition();

        //command should be processed successfully with result "OK"
        response = executeForward("HAL", Collections.singletonList("3"));
        position.y += 3;

        assertEquals(response.getResult(), "OK");

        //Robot should move the full distance of steps
        assertEquals("Done", response.getData().get("message"));
        assertEquals(position, World.getRobot("HAL").getPosition());
    }


    @Test
    void ForwardCommandEdgeObstructTest() {

        //Given default visibility = 5 & world size > visibility
        TestHelper.modifyWorld(new String[]{"-o=none","-s=1"});
        launchRobot("HAL", 5, 5);

        Point position = World.getRobot("HAL").getPosition();

        //command should be processed successfully with result "OK"
        response = executeForward("HAL", Collections.singletonList("1"));

        assertEquals(response.getResult(), "OK");

        //Robot should move the full distance of steps
        assertEquals("Obstructed", response.getData().get("message"));
        assertEquals(position, World.getRobot("HAL").getPosition());
    }

    @Test
    void ForwardCommandObstacleObstructTest() {

        //Given default visibility = 5 & world size > visibility
        TestHelper.modifyWorld(new String[]{"-o=0,1,1,1,1,0","-s=2"});
        launchRobot("HAL", 5, 5);

        Point position = World.getRobot("HAL").getPosition();

        //command should be processed successfully with result "OK"
        response = executeForward("HAL", Collections.singletonList("1"));

        assertEquals(response.getResult(), "OK");

        //Robot should move the full distance of steps
        assertEquals("Obstructed", response.getData().get("message"));
        assertEquals(position, World.getRobot("HAL").getPosition());
    }

    @Test
    void ForwardCommandMineObstructTest() {

        //Given default visibility = 5 & world size > visibility
        TestHelper.modifyWorld(new String[]{"-m=0,1,1,1,1,0","-s=2"});
        launchRobot("HAL", 5, 5);

        Point position = World.getRobot("HAL").getPosition();

        //command should be processed successfully with result "OK"
        response = executeForward("HAL", Collections.singletonList("1"));

        assertEquals(response.getResult(), "OK");

        //Robot should move the full distance of steps
        assertEquals("Mine", response.getData().get("message"));
        assertEquals(position, World.getRobot("HAL").getPosition());
    }

    @Test
    void ForwardCommandPitObstructTest() {

        //Given default visibility = 5 & world size > visibility
        TestHelper.modifyWorld(new String[]{"-pt=0,1,1,1,1,0","-s=2"});
        launchRobot("HAL", 5, 5);

        Point position = World.getRobot("HAL").getPosition();

        //command should be processed successfully with result "OK"
        response = executeForward("HAL", Collections.singletonList("1"));

        assertEquals(response.getResult(), "OK");

        //Robot should move the full distance of steps
        assertEquals("Fell", response.getData().get("message"));
        assertEquals(position, World.getRobot("HAL").getPosition());
    }

    @Test
    void ForwardCommandRobotObstructTest() {

        //Given default visibility = 5 & world size > visibility
        TestHelper.modifyWorld(new String[]{"-o=0,1,1,1","-s=2"});
        launchRobot("R1", 5, 5);
        launchRobot("R2", 5, 5);

        Point position_R1 = World.getRobot("R1").getPosition();
        Point position_R2 = World.getRobot("R2").getPosition();

        String robotToExecute = position_R1 == new Point(0,0) ? "R1" : "R2";

        //command should be processed successfully with result "OK"
        response = executeForward(robotToExecute, Collections.singletonList("1"));

        assertEquals(response.getResult(), "OK");

        //Robot should move the full distance of steps
        assertEquals("Obstructed", response.getData().get("message"));
        assertEquals(new Point(0,0) , World.getRobot(robotToExecute).getPosition());
    }

    private Response executeForward(String name, List<String> arguments) {
        command = new ForwardCommand();
        command.setRobot(name);
        command.setArguments(arguments);
        response = command.execute();
        return response;
    }

//    private Response executeForwardSuccessfully(String name, List<String> arguments) {
//        response = executeForward(name, arguments);
//
//    }





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
