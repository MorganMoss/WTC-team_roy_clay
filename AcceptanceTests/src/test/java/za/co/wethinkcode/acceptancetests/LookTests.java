package za.co.wethinkcode.acceptancetests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.acceptancetests.protocoldrivers.MockClient;
import za.co.wethinkcode.acceptancetests.protocoldrivers.MockServer;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LookTests {

    /**
     * As a player
     * I want to look around the Robot World & see Robot(s)/Obstacle(s) and other world artefacts in the way
     * So I can be aware of other Robot(s) in the world and avoid hitting Obstacle(s) & other artefacts in the world
     */
    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final MockClient serverClient = new MockClient();

    @AfterEach
    void disconnectServer(){
        serverClient.disconnect();
        MockServer.closeServer();
    }

    @Test
    void validLookAndWorldShouldBeEmpty(){

        //Given that I am connected to a running Robot Worlds server.
        //And there is no other robot and no obstacle in the world.
        MockServer.startServer("");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        assertTrue(serverClient.launchRobot("HAL"));

        //When I send a valid look request, "look", to the server.
        serverClient.sendRequest("HAL", "look", "[]");

        //Then I should get a valid/successful response, "result" = "OK" " from the server.
        Response response = serverClient.getResponse();
        serverClient.assertResult(response, "OK");

        //And the object field, which would contain present artefacts, should be empty.
        for (Map<String, Object> object : serverClient.getObjects(response)){
            assertEquals("EDGE", object.get("type"));
        }
    }

    @Test
    void robotShouldSeeAnObstacle(){

        //Given a world of size 2x2.
        //and the world has an obstacle at coordinate [0,1].
        MockServer.startServer("-s=2 -o=0,1 -pt=-1,1,-1,0,-1,-1,0,-1,1,-1,1,0,1,1 -v=3");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        assertTrue((serverClient.isConnected()));

        //and I have successfully launched a robot into the world
        assertTrue(serverClient.launchRobot("HAL"));

        //When I ask the robot to look
        serverClient.sendRequest("HAL", "look", "[]");

        //Then I should get a response back with an object of type OBSTACLE at a distance of 1 step.
        Response response = serverClient.getResponse();

        boolean foundObstacle = false;
        int distanceToObstacle = 0;
        String artefactFound = "";
        System.out.println(response.serialize());
        for (Map<String, Object> object : serverClient.getObjects(response)){
            if (((String) object.get("type")).equalsIgnoreCase("OBSTACLE")){
                foundObstacle = true;
                distanceToObstacle = serverClient.getAsInt(object, "distance");;
            }
        }
        assertTrue(foundObstacle);
        assertEquals(1, distanceToObstacle);
    }

    @Test
    void seeRobotsAndObstacles(){

        //Given a world of size 2x2.
        //and the world has an obstacle at coordinate [0,1].
        MockServer.startServer("-s=2 -o=0,1");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        assertTrue(serverClient.isConnected());


        //and I have successfully launched 8 robots into the world.
        String robotAtZeroZero = "";
        List<String> robotNames = Arrays.asList("R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8");
        for (String item : robotNames) {
            serverClient.sendRequest(item, "launch", "[\"shooter\",\"7\",\"4\"]");
            Response response = serverClient.getResponse();
            serverClient.assertResult(response, "OK");

            Point position = serverClient.getPosition(response.getState());
            if (0 == position.x && 0 == position.y) {
                robotAtZeroZero = item;
            }
            //find robot that lands at position (0,0)
        }

        //When I ask the robot at position (0,0) to look
        serverClient.sendRequest(robotAtZeroZero, "look", "[]");

        //Then I should get a valid response back with one object being an OBSTACLE that is one step away.
        //and three objects should be ROBOTs that are each one step away
        Response response = serverClient.getResponse();
        serverClient.assertResult(response, "OK");

        int numObstaclesSeen = 0;
        int numRobotsSeen = 0;
        int distanceToObstacle =0;
        int distanceToRobot =0;

        for (Map<String, Object> object : serverClient.getObjects(response)){
            if (((String) object.get("type")).equalsIgnoreCase("OBSTACLE")){
                numObstaclesSeen +=1;
                distanceToObstacle = serverClient.getAsInt(object, "distance");
                assertEquals(1, numObstaclesSeen);
                assertEquals(1, distanceToObstacle);
            }
            else if (((String) object.get("type")).equalsIgnoreCase("ROBOT")){
                numRobotsSeen +=1;
                distanceToRobot = serverClient.getAsInt(object, "distance");
                assertEquals(1, distanceToRobot);
            }

        }
        assertEquals(3, numRobotsSeen);
    }


    @Test
    void invalidLookCommandShouldFail(){
        MockServer.startServer("");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        //Given that I am connected to a running Robot Worlds server.
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        assertTrue(serverClient.launchRobot("HAL"));

        //When I send an invalid look request with a command such as "loook" instead of "look".
        serverClient.sendRequest("HAL", "loOok", "[]");

        //Then I should get an error result
        Response response = serverClient.getResponse();
        serverClient.assertResult(response, "ERROR");

        //And a message informing me that I entered an unsupported command
        serverClient.assertMessage(response, "Unsupported command");
    }

    @Test
    void invalidLookArgumentsShouldFail(){
        MockServer.startServer("");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        //Given that I am connected to a running Robot Worlds server.
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        assertTrue(serverClient.launchRobot("HAL"));

        //When I send an invalid look arguments (i.e. a non-empty list, because the world command does not take any arguments)
        serverClient.sendRequest("HAL", "look", "[height, width]");

        //Then I should get an error-  result
        Response response = serverClient.getResponse();
        serverClient.assertResult(response, "ERROR");

        //And a message informing me that server could not parse the request due to incorrect arguments
        serverClient.assertMessage(response, "Could not parse arguments");
    }
}