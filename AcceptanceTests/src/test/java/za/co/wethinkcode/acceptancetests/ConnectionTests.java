package za.co.wethinkcode.acceptancetests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.acceptancetests.protocoldrivers.MockServer;
import za.co.wethinkcode.acceptancetests.protocoldrivers.MockClient;

import static org.junit.jupiter.api.Assertions.*;



public class ConnectionTests {

    /**
     * As a player I want to be able to play the game with other players.
     * Thus I will connect to a server.
     */

    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final MockClient serverClient = new MockClient();

    @BeforeEach
    void startServer() {
        MockServer.startServer("-s=2 --port=" + DEFAULT_PORT);
    }

    @AfterEach
    void disconnectServer(){
        MockServer.closeServer();
        serverClient.disconnect();
    }

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
    }


    @Test
    void SingleClientConnectsToServerWithTheWrongIP() {

        // Given that the Port is the default used by the server
        assertEquals(5000, DEFAULT_PORT);

        // And the address of the server is not used to connect by the client, a bad address is used
        String BAD_IP = "bad IP";
        assertNotEquals("localhost", BAD_IP);

        // When the client tries to connect to the server
        serverClient.connect(BAD_IP, DEFAULT_PORT);

        // Then the client should not be connected to the server
        assertFalse(serverClient.isConnected());
    }


    @Test
    void SingleClientConnectsToServerWithTheWrongAddress() {

        // Given that the IP Address is the default used by the server
        assertEquals("localhost", DEFAULT_IP);

        // And the port of the server is not used to connect by the client, a bad port is used
        int BAD_PORT = 1000;
        assertNotEquals(5000, BAD_PORT);

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
    }


    @Test
    void YouConnectAfterAnotherClientConnects() {

        //Given that another client has connected using the Port and IP Address
        MockClient otherClient = new MockClient();
        otherClient.connect(DEFAULT_IP,DEFAULT_PORT);
        assertTrue(otherClient.isConnected());

        //And you are using the correct IP and Port
        assertEquals(5000, DEFAULT_PORT);
        assertEquals("localhost", DEFAULT_IP);

        // When you try to connect to the server
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);

        // Then the client should be connected to the server
        assertTrue(serverClient.isConnected());

        otherClient.disconnect();
    }


    @Test
    void ClientTalksToServerWhenMoreThanOneIsConnected() {

        //Given that you and another client has connected using the Port and IP Address
        assertEquals(5000, DEFAULT_PORT);
        assertEquals("localhost", DEFAULT_IP);

        MockClient otherClient = new MockClient();
        otherClient.connect(DEFAULT_IP,DEFAULT_PORT);
        assertTrue(otherClient.isConnected());

        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        assertTrue(serverClient.isConnected());

        //When you send a request to the server
        serverClient.ping();

        //Then only you should get a response
        assertNotNull(serverClient.getResponse());
        assertNull(otherClient.getResponse());

        otherClient.disconnect();
    }

}

