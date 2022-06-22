package za.co.wethinkcode.acceptancetests;

import com.fasterxml.jackson.databind.JsonNode;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldClient;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldJsonClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



public class LaunchRobotTests {

    /**
     * As a player
     * I want to launch my robot in the online robot world
     * So that I can break the record for the most robot kills
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
    void validLaunchShouldSucceed(){

        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        assertTrue(serverClient.launchRobot());

        // Then I should get a valid response, "OK".
        JsonNode response = serverClient.getResponse();
        serverClient.assertResult(response, "OK");

        // And the position should be (x:0, y:0)
        serverClient.assertPosition(response, 0, 0);

        // And I should also get the state of the robot
        assertNotNull(response.get("state"));
    }


    @Test
    void invalidLaunchShouldFail(){

        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // When I send an invalid launch request with the command "luanch" instead of "launch"
        serverClient.sendRequest("HAL", "luanch", "[\"shooter\",\"5\",\"5\"]");
        JsonNode response = serverClient.getResponse();

        // Then I should get an error response
        serverClient.assertResult(response, "ERROR");

        // And the message "Unsupported command"
        serverClient.assertMessage(response, "Unsupported command");
    }


    @Test
    void checkForDuplicateRobotName(){

        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // When I send a valid launch request with the robot name "HAL" to the server
        serverClient.sendRequest("HAL", "launch", "[\"shooter\",\"7\",\"4\"]");

        // Then I should get a valid response from the server
        JsonNode response_1 = serverClient.getResponse();
        serverClient.assertResult(response_1, "OK");

        // And I should also get the state of the robot
        assertNotNull(response_1.get("state"));

        // When I send another valid launch request with the same robot name "HAL" to the server
        serverClient.sendRequest("HAL", "launch", "[\"shooter\",\"5\",\"5\"]");

        // Then I should get an "ERROR" response from the server
        JsonNode response_2 = serverClient.getResponse();
        serverClient.assertResult(response_2, "ERROR");

        // And the message "Too many of you in this world"
        serverClient.assertMessage(response_2, "Too many of you in this world");
    }


    @Test
    void worldFullNoSpaceToLaunchRobot(){

        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        serverClient.sendRequest("HAL", "launch", "[\"shooter\",\"7\",\"4\"]");

        // Then I should get an "OK" response from the server
        JsonNode response_1 = serverClient.getResponse();
        serverClient.assertResult(response_1, "OK");

        // And the position should be (x:0, y:0)
        serverClient.assertPosition(response_1, 0, 0);

        // And I should also get the state for the launched robot
        assertNotNull(response_1.get("state"));

        // When I send another valid launch request for a different robot named "TRAVIS" to the server
        serverClient.sendRequest("TRAVIS", "launch", "[\"shooter\",\"7\",\"4\"]");

        // Then I should get an error response from the server
        JsonNode response_2 = serverClient.getResponse();
        serverClient.assertResult(response_2, "ERROR");

        // And the message "No more space in this world"
        serverClient.assertMessage(response_2, "No more space in this world");
    }


}
