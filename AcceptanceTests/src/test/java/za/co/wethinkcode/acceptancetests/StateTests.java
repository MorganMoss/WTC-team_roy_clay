package za.co.wethinkcode.acceptancetests;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.acceptancetests.protocoldrivers.MockServer;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldJsonClient;

import static org.junit.jupiter.api.Assertions.*;

public class StateTests {

    /**
     * As a player
     * I want to get the state of my robot
     * So that I can plan my next move
     */

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldJsonClient serverClient = new RobotWorldJsonClient();

    @BeforeEach
    void connectToServer(){
        // connects client to server before each test
        MockServer.startServer("");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }


    @AfterEach
    void disconnectFromServer(){
        // disconnects client from server after each test
        serverClient.disconnect();
        MockServer.closeServer();
    }


    @Test
    void validStateCommandShouldSucceed(){

        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        assertTrue(serverClient.launchRobot("PAL"));

        // When I send a valid state request to the server
        serverClient.sendRequest("PAL", "state");

        // Then I should get a valid response from server
        JsonNode response = serverClient.getResponse();

        serverClient.assertResult(response, "OK");

        //And get the state of robot

        assertNotNull(response.get("state").get("position"));
        assertEquals(0, response.get("state").get("position").get(0).asInt());
        assertEquals(0, response.get("state").get("position").get(1).asInt());
        assertNotNull(response.get("state").get("direction"));
        assertEquals("NORTH", response.get("state").get("direction").asText());
        assertNotNull(response.get("state").get("shields"));
//        assertEquals(3, response.get("state").get("shields").asInt());
        assertNotNull(response.get("state").get("shots"));
//        assertEquals(3, response.get("state").get("shots").asInt());
        assertNotNull(response.get("state").get("status"));
//        assertEquals("NORMAL", response.get("state").get("status").asText());
    }


    @Test
    void invalidStateCommandShouldFail() {

        //Given that I am connected to a running Robot Worlds server.
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        assertTrue(serverClient.launchRobot("MAL"));

        //When I send an invalid state request with a command such as "statte" instead of "state".
        serverClient.sendRequest("MAL", "statte");

        //Then I should get an "ERROR" response.
        JsonNode state_response = serverClient.getResponse();
        assertNotNull(state_response.get("result"));
        assertEquals("ERROR", state_response.get("result").asText());
        assertEquals("Unsupported command", state_response.get("data").get("message").asText());
    }


    @Test
    void invalidStateArgumentsShouldFail() {

        //Given that I am connected to a running Robot Worlds server.
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        assertTrue(serverClient.launchRobot("ZAL"));

        //When I send a valid state request with invalid arguments".
        serverClient.sendRequest("ZAL", "state", "[steps]");


        //Then I should get an "ERROR" response.
        JsonNode state_response = serverClient.getResponse();
        assertNotNull(state_response.get("result"));
        assertEquals("ERROR", state_response.get("result").asText());
        assertEquals("Could not parse arguments", state_response.get("data").get("message").asText());
    }


    @Test
    void stateCommandForInvalidRobot(){

        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // When I send a state request to a robot that has not been launched
        JsonNode response = serverClient.sendRequest("HANK", "state");

        // Then I should get an error message
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
    }

}
