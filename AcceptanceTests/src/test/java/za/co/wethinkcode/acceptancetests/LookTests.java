package za.co.wethinkcode.acceptancetests;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.acceptancetests.protocoldrivers.MockServer;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldClient;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldJsonClient;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LookTests {

    /**
     * As a player
     * I want to look around the Robot World & see Robot(s)/Obstacle(s) in the way
     * So I can avoid hitting Obstacle(s) & so I can be aware of other Robot(s) in the world
     */

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();


    @AfterEach
    void disconnectFromServer(){
        serverClient.disconnect();
        MockServer.closeServer();
    }

    @Test
    void validLookAndWorldShouldBeEmpty(){
        MockServer.startServer("");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        //Given that I am connected to a running Robot Worlds server.
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        assertTrue(serverClient.launchRobot("HAL"));

        //And there is no other robot and no obstacle in the world.
        //And I send a valid look request, "look", to the server.
        serverClient.sendRequest("HAL", "look", "[]");

        //Then I should get a valid/successful response, " "result" = "OK" " from the server.
        JsonNode response = serverClient.getResponse();
        serverClient.assertResult(response, "OK");

        //And the object field, which would contain present artefacts, should be empty.
        for (JsonNode item : response.get("data").get("objects")){
            assertEquals("EDGE", item.get("type").asText());
        }
    }

    @Test
    void robotShouldSeeAnObstacle(){
        MockServer.startServer("-s=2 -o=0,1,1,1,-1,1");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);

        //Given a world of size 2x2.
        //and the world has an obstacle at coordinate [0,1].
        assertTrue((serverClient.isConnected()));

        //and I have successfully launched a robot into the world
        assertTrue(serverClient.launchRobot("HAL"));

        //When I ask the first robot to look
        serverClient.sendRequest("HAL", "look", "[]");

        //Then I should get a response back with an object of type OBSTACLE at a distance of 1 step.
        JsonNode response = serverClient.getResponse();

        boolean foundObstacle = false;
        int distanceToObstacle = 0;
        String artefactFound = "";

        for (JsonNode object : response.get("data").get("objects")){
            if (object.get("type").asText().equalsIgnoreCase("OBSTACLE")){
                foundObstacle = true;
                distanceToObstacle = object.get("distance").asInt();
                artefactFound = "OBSTACLE";
            }
        }
        assertTrue(foundObstacle);
        assertEquals("OBSTACLE", artefactFound);
        assertEquals(1, distanceToObstacle);
    }

    @Test
    void seeRobotsAndObstacles(){
        List<String> robotNames = Arrays.asList("R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8");
        MockServer.startServer("-s=2 -o=0,1");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        //Given a world of size 2x2.
        //and the world has an obstacle at coordinate [0,1].
        assertTrue(serverClient.isConnected());

        //and I have successfully launched 8 robots into the world.
        for (String item : robotNames) {
            serverClient.sendRequest(item, "launch", "[\"shooter\",\"7\",\"4\"]");
            JsonNode response = serverClient.getResponse();
            serverClient.assertResult(response, "OK");
        }

        //When I ask the first robot to look
        serverClient.sendRequest("R1", "look", "[]");
        //TODO: check code base to see if robot is being placed randomly.

        //Then I should get a response back with one object being an OBSTACLE that is one step away.
        //and three objects should be ROBOTs that is one step away
        JsonNode response = serverClient.getResponse();
        serverClient.assertResult(response, "OK");

        int numObstaclesSeen = 0;
        int numRobotsSeen = 0;
        int distanceToObstacle =0;
        int distanceToRobot =0;

        for (JsonNode object : response.get("data").get("objects")){
            if (object.get("type").asText().equalsIgnoreCase("OBSTACLE")){
                numObstaclesSeen +=1;
                distanceToObstacle = object.get("distance").asInt();
            }
            else if (object.get("type").asText().equalsIgnoreCase("ROBOT")){
                numRobotsSeen +=1;
                distanceToRobot = object.get("distance").asInt();
            }
        }
        assertEquals(1, numObstaclesSeen);
        assertEquals(1, distanceToObstacle);
        assertEquals(3, numRobotsSeen);
        assertEquals(1, distanceToRobot);
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
        JsonNode response = serverClient.getResponse();
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
        JsonNode response = serverClient.getResponse();
        serverClient.assertResult(response, "ERROR");

        //And a message informing me that server could not parse the request due to incorrect arguments
        serverClient.assertMessage(response, "Invalid Request");
    }
}