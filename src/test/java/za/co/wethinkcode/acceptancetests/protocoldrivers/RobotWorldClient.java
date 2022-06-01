package za.co.wethinkcode.acceptancetests.protocoldrivers;

import com.fasterxml.jackson.databind.JsonNode;

public interface RobotWorldClient {
    void connect(String IPAddress, int port);

    void disconnect();

    boolean isConnected();

    void sendRequest(String request);

    JsonNode getResponse();

    void ping();
}
