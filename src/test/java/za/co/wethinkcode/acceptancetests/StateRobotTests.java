package za.co.wethinkcode.acceptancetests;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldClient;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldJsonClient;

import static org.junit.jupiter.api.Assertions.*;

public class StateRobotTests {

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();

    @BeforeEach
    void connectToServer(){
        // connects client to server before each test
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    void disconnectFromServer(){
        // disconnects client from server after each test
        serverClient.disconnect();
    }

    void launchRobot(){
        String launch_request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        serverClient.sendRequest(launch_request);
        JsonNode launch_response = serverClient.getResponse();
        assertNotNull(launch_response.get("result"));
        assertEquals("OK", launch_response.get("result").asText());
    }

    @Test
    void validStateCommandShouldSucceed(){
        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        launchRobot();

        // When I send a valid state request to the server
        String state_request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"state\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        serverClient.sendRequest(state_request);

        // Then I should get a valid response from server
        JsonNode state_response = serverClient.getResponse();
        assertEquals("OK", state_response.get("result").asText());

        // And get the state of robot
        assertNotNull(state_response.get("state").get("position"));
        assertEquals(0, state_response.get("state").get("position").get(0).asInt());
        assertEquals(0, state_response.get("state").get("position").get(1).asInt());
        assertNotNull(state_response.get("state").get("direction"));
        assertEquals("NORTH", state_response.get("state").get("direction").asText());
        assertNotNull(state_response.get("state").get("shields"));
        assertEquals(0, state_response.get("state").get("shields").asInt());
        assertNotNull(state_response.get("state").get("shots"));
        assertEquals(0, state_response.get("state").get("shots").asInt());
        assertNotNull(state_response.get("state").get("status"));
        assertEquals("TODO", state_response.get("state").get("status").asText());
    }

    @Test
    void invalidStateCommandShouldFail() {
        //Given that I am connected to a running Robot Worlds server.
        assertTrue(serverClient.isConnected());

        //And I have successfully launched a robot to the server
        launchRobot();

        //When I send an invalid state request with a command such as "statte" instead of "state".
        String state_request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"statte\"," +
                "  \"arguments\": []" +
                "}";
        serverClient.sendRequest(state_request);

        //Then I should get an "ERROR" response.
        JsonNode state_response = serverClient.getResponse();
        assertNotNull(state_response.get("result"));
        assertEquals("ERROR", state_response.get("result").asText());
        assertTrue(state_response.get("data").get("message").asText().contains("Unsupported command"));
    }

    @Test
    void stateCommandForInvalidRobot(){

        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());


        // When I send a state request to a robot that has not been launched
        String request = "{" +
                "\"robot\": \"\"," +
                "\"command\": \"state\"," +
                "\"arguments\": []" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an error message
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());


        // And the message "Robot does not exist"
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertTrue(response.get("data").get("message").asText().contains("Robot does not exist"));
    }
}
