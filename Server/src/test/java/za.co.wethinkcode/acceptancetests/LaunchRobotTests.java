package za.co.wethinkcode.acceptancetests;

import com.fasterxml.jackson.databind.JsonNode;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldClient;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldJsonClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * As a player
 * I want to launch my robot in the online robot world
 * So that I can break the record for the most robot kills
 */

public class LaunchRobotTests {

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

    /**
     * Below are functions to assert arguments and avoid repeating lines of code in tests
     */

//    private void assertResult(JsonNode response, String status){
//        assertNotNull(response.get("result"));
//        assertEquals(status, response.get("result").asText());
//    }

    private void assertMessage(JsonNode response, String message){
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertTrue(response.get("data").get("message").asText().contains(message));
    }

    private void assertPosition(JsonNode response, int x, int y){
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("position"));
        assertEquals(x, response.get("data").get("position").get(0).asInt());
        assertEquals(y, response.get("data").get("position").get(1).asInt());
    }

    @Test
    void validLaunchShouldSucceed(){

        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an "OK" response
        serverClient.assertResult(response, "OK");

        // And the position should be (x:0, y:0)
        assertPosition(response, 0, 0);

        // And I should also get the state of the robot
        assertNotNull(response.get("state"));
    }


    @Test
    void invalidLaunchShouldFail(){

        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // When I send an invalid launch request with the command "luanch" instead of "launch"
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"luanch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an error response
        serverClient.assertResult(response, "ERROR");

        // And the message "Unsupported command"
        assertMessage(response, "Unsupported command");
    }


    @Test
    void checkForDuplicateRobotName(){

        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        serverClient.assertResult(response, "OK");

        // And I should also get the state of the robot
        assertNotNull(response.get("state"));

        // When I send another valid launch request with the same robot name "HAL" to the server
        String request_2 = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"7\",\"4\"]" +
                "}";

        JsonNode response_2 = serverClient.sendRequest(request_2);

        // Then I should get an invalid or error response from the server
        serverClient.assertResult(response, "ERROR");

        // And the message "Too many of you in this world"
        assertMessage(response_2, "Too many of you in this world");
    }

    @Test
    void worldFullNoSpaceToLaunchRobot(){

        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";

        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        serverClient.assertResult(response, "OK");

        // And the position should be (x:0, y:0)
        assertPosition(response, 0, 0);

        // And I should also get the state for the launched robot
        assertNotNull(response.get("state"));

        // When I send another valid launch request for a different robot named "TRAVIS" to the server
        String request_2 = "{" +
                "  \"robot\": \"TRAVIS\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"7\",\"4\"]" +
                "}";

        JsonNode response_2 = serverClient.sendRequest(request_2);

        // Then I should get an invalid or error response from the server
        serverClient.assertResult(response, "ERROR");

        // And the message "No more space in this world"
        assertMessage(response_2, "No more space in this world");
    }
}
