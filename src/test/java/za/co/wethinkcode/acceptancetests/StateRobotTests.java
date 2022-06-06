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

    @Test
    void invalidStateCommandShouldFail(){
        assertTrue(serverClient.isConnected());

        // When I send an invalid state request with the command "statte" instead of "state"
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"statte\"," +
                "\"arguments\": []" +
                "}";
        serverClient.sendRequest(request);
        JsonNode response = serverClient.getResponse();

        // Then I should get an error message
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // And the message "Unsupported command"
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertTrue(response.get("data").get("message").asText().contains("Unsupported command"));
    }

        @Test
        void validStateCommandShouldSucceed(){
            // Given that I am connected to a running Robot Worlds server
            assertTrue(serverClient.isConnected());

            // When I send a valid state request to the server
            // And the robot does exist in the world
            String request = "{" +
                    "  \"robot\": \"HAL\"," +
                    "  \"command\": \"state\"," +
                    "  \"arguments\": []" +
                    "}";
            serverClient.sendRequest(request);
            JsonNode response = serverClient.getResponse();
            assertEquals("ERROR", response.get("result").asText());

            //Then I should receive the state of the robot
            assertNotNull(response.get("state"));

        }

        @Test
        void stateCommandForInvalidRobot(){

            // Given that I am connected to a running Robot Worlds server
            assertTrue(serverClient.isConnected());


            // When I send a valid state request to a robot that has not been launched
            // And the robot does not exist in the world



            // Then I should get an empty list of robots state as a response

        }
}
