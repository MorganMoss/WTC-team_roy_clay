package za.co.wethinkcode.acceptancetests.protocoldrivers;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A basic interface that wraps the expected Robot Worlds API so that we can easily connect to and send requests to the server.
 */
public interface RobotWorldClient {

    /**
     * Connects to the Robot Worlds server on specified ip address and port
     * @param IPAddress either `localhost` or actual IP address
     * @param port port that server is configured to receive connections on
     */
    void connect(String IPAddress, int port);

    /**
     * Disconnect from server
     */
    void disconnect();

    /**
     * Checks if client is connected to server
     * @return true if connected
     */
    boolean isConnected();

    /**
     * Sends a request presented by the Json object to the server
     * @param requestString a String representing the Json string to send to server
     * @return the response as a JsonNode
     */
    JsonNode sendRequest(String requestString);

    JsonNode sendRequest(String robot, String command, String args);

    /**
     * get a response from the server
     * @return JsonNode serialised object
     */
    JsonNode getResponse();

    /**
     * send a ping to the server to check if it is alive
     */
    void ping();

    boolean launchRobot();
    boolean launchRobot(String name);

    void assertResult(JsonNode response, String status);

    void assertMessage(JsonNode response, String message);

    void assertPosition(JsonNode response, int x, int y);

    int getX(JsonNode response);

    int getY(JsonNode response);


}
