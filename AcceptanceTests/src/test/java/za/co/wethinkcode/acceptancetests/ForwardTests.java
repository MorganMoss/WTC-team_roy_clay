package za.co.wethinkcode.acceptancetests;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.acceptancetests.protocoldrivers.MockServer;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldClient;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldJsonClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ForwardTests {

    /**
     * As a player
     * I want to command my robot to move forward a specified number of steps
     * so that I can explore the world and not be a sitting duck in a battle.
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

    @BeforeEach
    void startServer() {
        MockServer.startServer("-s=1");

    }

    @AfterEach
    void getResult(){
        MockServer.closeServer();
    }

    @Test
    void movingAtTheEdgeOfTheWorldShouldFail(){

        //Given that I am connected to a running Robot Worlds server.
        //And the world is of size 1x1 with no obstacles or pits.
        assertTrue(serverClient.isConnected());

        //And a robot called "HAL" is already connected and launched.
        assertTrue(serverClient.launchRobot("HAL"));

        //When I send a command for "HAL" to move forward by 5 steps.
        serverClient.sendRequest("HAL", "forward", "[5]");

        //Then I should get an "OK" response with the message "At the NORTH edge"
        JsonNode response = serverClient.getResponse();
        serverClient.assertResult(response, "OK");
        serverClient.assertMessage(response, "At the NORTH edge");

        //and the position information returned should be at co-ordinates [0,0]
        serverClient.assertPosition(response,0,0);

    }
}
