package za.co.wethinkcode.acceptancetests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.acceptancetests.protocoldrivers.MockServer;
import za.co.wethinkcode.acceptancetests.protocoldrivers.MockClient;

import static org.junit.jupiter.api.Assertions.*;

public class StateTests {

    /**
     * As a player
     * I want to get the state of my robot
     * So that I can plan my next move
     */

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final MockClient serverClient = new MockClient();

    @BeforeEach
    void connectToServer(){
        // connects client to server before each test
        MockServer.startServer("");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }


    @AfterEach
    void disconnectServer(){
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
        Response response = serverClient.getResponse();

        serverClient.assertResult(response, "OK");

        //And get the state of robot

        assertNotNull(response.getState().get("position"));
        serverClient.assertPosition(response,0,0);
        assertNotNull(response.getState().get("direction"));
        assertEquals("NORTH", response.getState().get("direction"));
        assertNotNull(response.getState().get("shields"));
        assertNotNull(response.getState().get("shots"));
        assertNotNull(response.getState().get("status"));
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
        Response state_response = serverClient.getResponse();
        assertNotNull(state_response.getResult());
        assertEquals("ERROR", state_response.getResult());
        assertEquals("Unsupported command", state_response.getData().get("message"));
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
        Response state_response = serverClient.getResponse();
        assertNotNull(state_response.getResult());
        assertEquals("ERROR", state_response.getResult());
        assertEquals("Could not parse arguments", state_response.getData().get("message"));
    }


    @Test
    void stateCommandForInvalidRobot(){

        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // When I send a state request to a robot that has not been launched
        Response response = serverClient.sendRequest("HANK", "state");

        // Then I should get an error message
        assertNotNull(response.getResult());
        assertEquals("ERROR", response.getResult());
    }

}
