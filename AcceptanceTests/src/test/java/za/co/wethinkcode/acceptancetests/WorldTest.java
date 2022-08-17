package za.co.wethinkcode.acceptancetests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.acceptancetests.protocoldrivers.MockClient;
import za.co.wethinkcode.acceptancetests.protocoldrivers.MockServer;

import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {

    /**
     * As a player
     * I want to view the worlds parameters (defining characteristics)
     * So that I can understand the environment (Robot World) better & win the game.
     */

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final MockClient serverClient = new MockClient();

    @AfterEach
    void disconnectFromServer(){
        serverClient.disconnect();
        MockServer.closeServer();
    }

    @Test
    void invalidWorldCommandShouldFail(){
        MockServer.startServer("");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);

        //Given that I am connected to a running Robot Worlds server.
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        assertTrue(serverClient.isConnected());

        //When I send an invalid world request with a command such as "wold" instead of "world".
        serverClient.sendRequest("any", "wold");

        //Then I should get an error result
        Response response = serverClient.getResponse();
        assertNotNull(response.getResult());
        assertEquals("ERROR", response.getResult());

        //And a message informing me that I entered an unsupported command
        assertEquals("Unsupported command", response.getData().get("message"));

    }


    @Test
    void invalidWorldArgumentsShouldFail(){
        MockServer.startServer("");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);

        //Given that I am connected to a running Robot Worlds server.
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        assertTrue(serverClient.isConnected());

        //When I send an invalid world arguments (i.e. a non-empty list, because the world command does not take any arguments)
        serverClient.sendRequest("any", "world", "[non-empty]");

        //Then I should get an error result
        Response response = serverClient.getResponse();
        assertNotNull(response.getResult());
        assertEquals("ERROR", response.getResult());

        //And a message informing me that server could not parse the request due to incorrect arguments
        assertEquals("Could not parse arguments", response.getData().get("message"));

    }

    @Test
    void validWorldCommandShouldReturnWorldArtefacts(){
        MockServer.startServer("");
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);

        //Given that I am connected to a running Robot Worlds server.
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        //list other configurations like visibility, reload, repair, mine, max-shield & max shots here.

        assertTrue(serverClient.isConnected());

        //When I send valid world request


        serverClient.sendRequest("any", "world");

        //Then I should get a valid response from the server
        Response response = serverClient.getResponse();
        assertNotNull(response.getResult());
        assertEquals("OK", response.getResult());

        //And information about the defining characteristics of Robot World
        assertNotNull(response.getData());

    }
}



