package za.co.wethinkcode.acceptancetests;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldClient;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldJsonClient;
import java.util.List;
import java.util.Arrays;
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
    void connectToServer() {
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }


    @AfterEach
    void disconnectFromServer() {
        serverClient.disconnect();
    }


    @Test
    void validLaunchShouldSucceed() {

        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1
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
    void invalidLaunchShouldFail() {

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
    void checkForDuplicateRobotName() {

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
    void worldFullNoSpaceToLaunchRobot() {

        // Given that I am connected to a running Robot Worlds server
        //And the world is of size 1x1
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


    @Test
    void canLaunchAnotherRobot() {
        // Given a world of size 2x2
        // and robot "HAL" has already been launched into the world
        assertTrue(serverClient.isConnected());
        assertTrue(serverClient.launchRobot("HAL"));

        // When I launch robot "R2D2" into the world
        serverClient.sendRequest("R2D2", "launch", "[\"shooter\",\"7\",\"4\"]");


        // Then the launch should be successful
        JsonNode response = serverClient.getResponse();
        serverClient.assertResult(response, "OK");
        assertNotNull(response.get("state"));

        // and a randomly allocated position of R2D2 should be returned.
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("position"));

        assertTrue(response.get("data").get("position").get(1).isInt());
        assertTrue(response.get("data").get("position").get(0).isInt());


        assertTrue(serverClient.getX(response) >= -1 && serverClient.getX(response) <= 1);
        assertTrue(serverClient.getY(response) >= -1 && serverClient.getY(response) <= 1);
    }


    @Test
    void worldWithoutObsIsFull() {

        List<String> robotNames = Arrays.asList("R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9");

        // Given a world of size 2x2
        // and I have successfully launched 9 robots into the world
        assertTrue(serverClient.isConnected());
        for (String item : robotNames) {
            serverClient.sendRequest(item, "launch", "[\"shooter\",\"7\",\"4\"]");
        }

        // when I launch one more robot
        serverClient.sendRequest("R10", "launch", "[\"shooter\",\"7\",\"4\"]");

        // Then I should get an ""ERROR" response
        JsonNode response = serverClient.getResponse();
        serverClient.assertResult(response, "ERROR");

        // with the message "No more space in this world".
        serverClient.assertMessage(response, "No more space in this world");
    }


    @Test
    void launchRobotWithObs() {

        List<String> robotNames = Arrays.asList("R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8");

        // Given a world of size 2x2
        // and the world has an obstacle at coordinate [1,1]
        assertTrue(serverClient.isConnected());

        // When I launch 8 robots into the world
        // Then each robot cannot be in position [1,1]
        boolean noRobotAtPosition11 = true;
        while (noRobotAtPosition11) {
            for (String item : robotNames) {
                serverClient.sendRequest(item, "launch", "[\"shooter\",\"7\",\"4\"]");
                JsonNode response = serverClient.getResponse();
                if (response.get("result").asText().equalsIgnoreCase("ERROR")){
                    noRobotAtPosition11 = false;
                }
            }
        }
    }


    @Test
    void WorldWithAnObstacleIsFull() {

        List<String> robotNames = Arrays.asList("R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8");

        // Given a world of size 2x2
        // and the world has an obstacle at coordinate [1,1]
        assertTrue(serverClient.isConnected());

        // and I have successfully launched 8 robots into the world

        for (String item : robotNames) {
            serverClient.sendRequest(item, "launch", "[\"shooter\",\"7\",\"4\"]");
            JsonNode response = serverClient.getResponse();
            serverClient.assertResult(response, "OK");
        }

        //When I launch one more robot
        serverClient.sendRequest("R9", "launch", "[\"shooter\",\"7\",\"4\"]");

        //Then I should get an error response back with the message "No more space in this world"
        JsonNode response = serverClient.getResponse();
        serverClient.assertResult(response, "ERROR");

        //with the message "No more space in this world"
        serverClient.assertMessage(response, "No more space in this world");

    }
}