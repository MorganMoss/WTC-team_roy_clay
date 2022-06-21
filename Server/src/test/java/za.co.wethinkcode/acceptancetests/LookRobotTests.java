package za.co.wethinkcode.acceptancetests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldClient;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldJsonClient;


import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class LookRobotTests {

    /**
     * As a player
     * I want to look around the Robot World & see Robot(s)/Obstacle(s) in the way
     * So I can avoid hitting Obstacle(s) & so I can be aware of other Robot(s) in the world
     */

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();

    @BeforeEach
    void connectToServer(){serverClient.connect(DEFAULT_IP, DEFAULT_PORT);}

    @AfterEach
    void disconnectFromServer(){
        serverClient.disconnect();
    }

    @Test
    void invalidLookCommandShouldFail(){

        //Given that I am connected to a running Robot Worlds server.
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        assertTrue(serverClient.launchRobot());

//        When I send an invalid look request with a command such as "loook" instead of "look".
        serverClient.sendRequest("HAL", "loOok", "[]");

        //Then I should get an error result
        JsonNode look_response = serverClient.getResponse();
        assertNotNull(look_response.get("result"));
        assertEquals("ERROR", look_response.get("result").asText());

        //And a message informing me that I entered an unsupported command
        assertEquals("Unsupported command", look_response.get("data").get("message").asText());
    }

    @Test
    void invalidLookArgumentsShouldFail(){

        //Given that I am connected to a running Robot Worlds server.
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        assertTrue(serverClient.launchRobot("NOT HAL"));

        //When I send an invalid world arguments (i.e. a non-empty list, because the world command does not take any arguments)
        serverClient.sendRequest("NOT HAL", "look", "[height, width]");

        //Then I should get an error result
        JsonNode response = serverClient.getResponse();
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());

        //And a message informing me that server could not parse the request due to incorrect arguments
        assertEquals("Invalid Request", response.get("data").get("message").asText());

    }

    @Test
    void validLookNoOtherArtifacts(){

        //Given that I am connected to a running Robot Worlds server.
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        assertTrue(serverClient.launchRobot());

        //And there is no other robot and no obstacle in the world.
        //And I send a valid look request, "look", to the server.
        serverClient.sendRequest("HAL", "look", "[]");

        //Then I should get a valid/successful response, " "result" = "OK" " from the server.
        JsonNode look_response = serverClient.getResponse();
        assertNotNull(look_response.get("result"));
        assertEquals("OK", look_response.get("result").asText());

         //And the object field, which contains would contain present artefacts, should be empty.
        for (JsonNode item : look_response.get("data").get("objects")){
            assertEquals("EDGE", item.get("type").asText());
        }

    }


    @Test
    void validLookOtherArtifacts(){

        //Given that I am connected to a running Robot Worlds server.
        //And I have a world with size > 1x1 (in this case 10x10) and an obstacle at (0, 1)
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        assertTrue(serverClient.launchRobot());

        //And I send a valid look request, "look", to the server.
        serverClient.sendRequest("HAL", "look", "[]");

        //Then I should get a valid/successful response, " "result" = "OK" " from the server.
        JsonNode look_response = serverClient.getResponse();
        assertNotNull(look_response.get("result"));
        assertEquals("OK", look_response.get("result").asText());


        boolean found_obstacle = false;
//        //And the object field, which contains would contain present artefacts, should be empty.
        for (JsonNode item : look_response.get("data").get("objects")){
            assertTrue("EDGE".equalsIgnoreCase(item.get("type").asText())
                    || "OBSTACLE".equalsIgnoreCase(item.get("type").asText()));
            System.out.println(item.get("type").asText());
            if (item.get("type").asText().equalsIgnoreCase("OBSTACLE")){
                if (found_obstacle){
                    fail();
                }
                found_obstacle = true;
            }
        }
        assertTrue(found_obstacle);

    }


}

