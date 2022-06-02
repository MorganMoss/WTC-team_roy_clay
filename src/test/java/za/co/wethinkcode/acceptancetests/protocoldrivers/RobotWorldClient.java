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
     * @param requestJsonString a String representing the Json string to send to server
     * @return the response as a JsonNode
     */
    void sendRequest(String requestJsonString);

    JsonNode getResponse();

    void ping();
}
