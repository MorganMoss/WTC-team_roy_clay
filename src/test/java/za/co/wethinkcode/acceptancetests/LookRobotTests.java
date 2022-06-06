package za.co.wethinkcode.acceptancetests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldClient;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldJsonClient;

import com.fasterxml.jackson.databind.JsonNode;


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
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        String launch_request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        serverClient.sendRequest(launch_request);
        JsonNode launch_response = serverClient.getResponse();
        assertNotNull(launch_response.get("result"));
        assertEquals("OK", launch_response.get("result").asText());

        //When I send an invalid look request with a command such as "loook" instead of "look".
        String look_request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"loook\"," +
                "  \"arguments\": []" +
                "}";
        serverClient.sendRequest(look_request);

        //Then I should get an "ERROR" response.
        JsonNode look_response = serverClient.getResponse();
        assertNotNull(look_response.get("result"));
        assertEquals("ERROR", look_response.get("result").asText());

    }


    @Test
    void validLookOtherArtifacts(){

        //Given that I am connected to a running Robot Worlds server.
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        String launch_request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        serverClient.sendRequest(launch_request);
        JsonNode launch_response = serverClient.getResponse();
        assertNotNull(launch_response.get("result"));
        assertEquals("OK", launch_response.get("result").asText());


        //And there is no other robot and no obstacle in the world.
        //And I send a valid look request, "look", to the server.
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        serverClient.sendRequest(request);

        //Then I should get a valid/successful response, " "result" = "OK" " from the server.
        JsonNode look_response = serverClient.getResponse();
        assertNotNull(look_response.get("result"));
        assertEquals("OK", look_response.get("result").asText());

        //And the object field, which contains would contain present artefacts, should be empty.
        assertNotNull(look_response.get("data").get("objects"));

    }


    @Test
    void validLookNoOtherArtifacts(){

        //Given that I am connected to a running Robot Worlds server.
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        String launch_request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        serverClient.sendRequest(launch_request);
        JsonNode launch_response = serverClient.getResponse();
        assertNotNull(launch_response.get("result"));
        assertEquals("OK", launch_response.get("result").asText());


        //And there is no other robot and no obstacle in the world.






        //And I send a valid look request, "look", to the server.
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        serverClient.sendRequest(request);

        //Then I should get a valid/successful response, " "result" = "OK" " from the server.
        JsonNode look_response = serverClient.getResponse();
        assertNotNull(look_response.get("result"));
        assertEquals("OK", look_response.get("result").asText());

        //And the object field, which contains would contain present artefacts, should be empty.
        assertNull(look_response.get("data").get("objects"));

    }


}

