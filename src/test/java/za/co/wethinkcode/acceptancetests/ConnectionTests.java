package za.co.wethinkcode.acceptancetests;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldClient;
import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldJsonClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * As a player I want to be able to play the game with other players.
 * Thus I will connect to a server.
 */
public class TestServerAndClientConnectAndCommunicate {
    private final static int DEFAULT_PORT = 5000;

    private final static String DEFAULT_IP = "localhost";

    private final RobotWorldClient serverClient = new RobotWorldJsonClient();

    @Test
    void SingleClientConnectsToServer() {
        // Given that the Port and IP Address
        // are the default ones used by the server
        assertEquals(5000, DEFAULT_PORT);
        assertEquals("localhost", DEFAULT_IP);

        // When the client tries to connect to the server
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);

        // Then the client should be connected to the server
        assertTrue(serverClient.isConnected());

        serverClient.disconnect();
    }

    @Test
    void SingleClientConnectsToServerWithTheWrongIP() {
        // Given that the Port is the default used by the server
        // And the address of the server is not used to connect by the client, a bad address is used
        String BAD_IP = "bad IP";

        assertEquals(5000, DEFAULT_PORT);
        assertNotEquals("localhost", BAD_IP);

        // When the client tries to connect to the server
        serverClient.connect(BAD_IP, DEFAULT_PORT);

        // Then the client should not be connected to the server
        assertFalse(serverClient.isConnected());
    }

    @Test
    void SingleClientConnectsToServerWithTheWrongAddress() {
        // Given that the IP Adress is the default used by the server
        // And the port of the server is not used to connect by the client, a bad port is used
        int BAD_PORT = 1000;

        assertNotEquals(5000, BAD_PORT);
        assertEquals("localhost", DEFAULT_IP);

        // When the client tries to connect to the server
        serverClient.connect(DEFAULT_IP, BAD_PORT);

        // Then the client should not be connected to the server
        assertFalse(serverClient.isConnected());
    }

    @Test
    void SingleClientTalksToServer() {
        //Given that you connect to the server with the correct IP and Port
        assertEquals(5000, DEFAULT_PORT);
        assertEquals("localhost", DEFAULT_IP);
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        assertTrue(serverClient.isConnected());

        //When you send a request
        serverClient.ping();

        //Then you should receive a response
        assertNotNull(serverClient.getResponse());

        serverClient.disconnect();
    }

    @Test
    void YouConnectAfterAnotherClientConnects() {
        //Given that another client has connected using the Port and IP Address
        //And you are using the correct IP and Port
        assertEquals(5000, DEFAULT_PORT);
        assertEquals("localhost", DEFAULT_IP);

        RobotWorldClient otherClient = new RobotWorldJsonClient();
        otherClient.connect(DEFAULT_IP,DEFAULT_PORT);
        assertTrue(otherClient.isConnected());

        // When you try to connect to the server
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);

        // Then the client should be connected to the server
        assertTrue(serverClient.isConnected());

        serverClient.disconnect();
        otherClient.disconnect();
    }

    @Test
    void ClientTalksToServerWhenMoreThanOneIsConnected() {
        //Given that you and another client has connected using the Port and IP Address
        assertEquals(5000, DEFAULT_PORT);
        assertEquals("localhost", DEFAULT_IP);

        RobotWorldClient otherClient = new RobotWorldJsonClient();
        otherClient.connect(DEFAULT_IP,DEFAULT_PORT);
        assertTrue(otherClient.isConnected());

        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        assertTrue(serverClient.isConnected());

        //When you send a request to the server
        serverClient.ping();

        //Then only you should get a response
        assertNotNull(serverClient.getResponse());
        assertNull(otherClient.getResponse());

        serverClient.disconnect();
        otherClient.disconnect();
    }
}

