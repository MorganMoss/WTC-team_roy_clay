package za.co.wethinkcode.acceptancetests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldClient;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldJsonClient;

import com.fasterxml.jackson.databind.JsonNode;


import static org.junit.Assert.*;
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
    void connectToServer(){
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    void disconnectFromServer(){
        serverClient.disconnect();
    }

    @Test
    void invalidLookCommandShouldFail(){

        //Given that I am connected to a running Robot Worlds server.
        assertTrue(serverClient.isConnected());

        //When I send an invalid look request with a command such as "loook" instead of "look".
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"loook\"," +
                "  \"arguments\": []" +
                "}";
        serverClient.sendRequest(request);

        //Then I should get an "ERROR" response.
        JsonNode response = serverClient.getResponse();
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());

    }


    @Test
    void validLookNoOtherArtifacts(){

        //Given that I am connected to a running Robot Worlds server.
        assertTrue(serverClient.isConnected());

        //And there is no other robot and no obstacle in the world.
        //And I send a valid look request, "look", to the server.
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        serverClient.sendRequest(request);

        //Then I should get a valid/successful response, " "result" = "OK" " from the server.
        JsonNode response = serverClient.getResponse();
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        //And the object field, which contains would contain present artefacts, should be empty.
        assertNull(response.get("data"));

    }

    @Test
    void validLookOtherArtifacts(){

        //Given that I am connected to a running Robot Worlds server.
        assertTrue(serverClient.isConnected());

        //And I send a valid look request, "look", to the server.
        String request =  "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";

        serverClient.sendRequest(request);

//        And there is at least one obstacle or at least one other robot in the world.
//            how do I mock the case where there are obstacles?

        //Then I should get a valid/successful response, " "result" = "OK" " from the server.
        JsonNode response = serverClient.getResponse();
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        //And the object field, which contains would contain present artefacts, should return details about present arterfacts.
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("objects"));
    }
}

