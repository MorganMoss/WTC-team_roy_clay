//package za.co.wethinkcode.acceptancetests;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldClient;
//import za.co.wethinkcode.acceptancetests.protocoldrivers.RobotWorldJsonClient;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class WorldTest {
//
//    /**
//     * As a player
//     * I want to view the worlds parameters (defining characteristics)
//     * So that I can understand the environment (Robot World) better & win the game.
//     */
//
//    private final static int DEFAULT_PORT = 5000;
//    private final static String DEFAULT_IP = "localhost";
//    private final RobotWorldClient serverClient = new RobotWorldJsonClient();
//
//    @BeforeEach
//    void connectToServer(){
//        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
//    }
//
//    @AfterEach
//    void disconnectFromServer(){
//        serverClient.disconnect();
//    }
//
//    @Test
//    void invalidWorldCommandShouldFail(){
//
//        //Given that I am connected to a running Robot Worlds server.
//        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
//        assertTrue(serverClient.isConnected());
//
//        //When I send an invalid world request with a command such as "wold" instead of "world".
//        String request = "{" +
//                "\"robot\": \"any\"," +
//                "\"command\": \"wold\"," +
//                "  \"arguments\": []" +
//                "}";
//        serverClient.sendRequest(request);
//
//        //Then I should get an error result
//        JsonNode response = serverClient.getResponse();
//        assertNotNull(response.get("result"));
//        assertEquals("ERROR", response.get("result").asText());
//
//        //And a message informing me that I entered an unsupported command
//        assertEquals("Unsupported command", response.get("data").get("message").asText());
//
//    }
//
//
//    @Test
//    void invalidWorldArgumentsShouldFail(){
//
//        //Given that I am connected to a running Robot Worlds server.
//        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
//        assertTrue(serverClient.isConnected());
//
//        //When I send an invalid world arguments (i.e. a non-empty list, because the world command does not take any arguments)
//        String request = "{" +
//                "\"robot\": \"any\"," +
//                "\"command\": \"world\"," +
//                "  \"arguments\": [height, width]" +
//                "}";
//        serverClient.sendRequest(request);
//
//        //Then I should get an error result
//        JsonNode response = serverClient.getResponse();
//        assertNotNull(response.get("result"));
//        assertEquals("ERROR", response.get("result").asText());
//
//        //And a message informing me that server could not parse the request due to incorrect arguments
//        assertEquals("Invalid Request", response.get("data").get("message").asText());
//
//    }
//
//    @Test
//    void validWorldCommandShouldReturnWorldArtefacts(){
//
//        //Given that I am connected to a running Robot Worlds server.
//        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
//        //list other configurations like visibility, reload, repair, mine, max-shield & max shots here.
//
//        assertTrue(serverClient.isConnected());
//
//        //When I send valid world request
//        String world_request = "{" +
//                "\"robot\": \"any\"," +
//                "\"command\": \"world\"," +
//                "  \"arguments\": []" +
//                "}";
//
//        serverClient.sendRequest(world_request);
//
//        //Then I should get a valid response from the server
//        JsonNode response = serverClient.getResponse();
//        assertNotNull(response.get("result"));
//        assertEquals("OK", response.get("result").asText());
//
//        //And information about the defining characteristics of Robot World
//        assertEquals(1, response.get("data").get("dimensions").get(0).asInt());
//        assertEquals(1, response.get("data").get("dimensions").get(0).asInt());
//        assertEquals(1, response.get("data").get("visibility").asInt());
//        assertEquals(1, response.get("data").get("reload").asInt());
//        assertEquals(1, response.get("data").get("mine").asInt());
//        assertEquals(1, response.get("data").get("max-shields").asInt());
//        assertEquals(1, response.get("data").get("max-shots").asInt());
//
//    }
//
//
//
//}
//
//
//
