package za.co.wethinkcode.acceptancetests.protocoldrivers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;

public class RobotWorldJsonClient implements RobotWorldClient {
//    private JsonNode lastResponse;

    private Socket socket;
    private boolean connected = false;

    private BufferedReader responses;
    private PrintStream requests;
    public RobotWorldJsonClient(){
    }

    private void setupComms(){
        try {
//            responses = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            requests = new PrintStream(socket.getOutputStream());
        } catch (IOException ignored) {
        }
    }
    @Override
    public void connect(String IPAddress, int port) {
        try {
            this.socket = new Socket(IPAddress, port);
            connected = true;
        } catch (IOException notConnected) {
            connected = false;
        }

        setupComms();
    }

    @Override
    public void disconnect() {
        try {
            responses.close();
            requests.close();
            socket.close();
        } catch (IOException e) {
            //error connecting should just throw Runtime error and fail test
            throw new RuntimeException("Error disconnecting from Robot Worlds server.", e);
        }
        connected = false;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void sendRequest(String request) {
        requests.print(request);
        requests.flush();
    }

    @Override
    public String sendRequestAsString(String requestString) {
        try {
            requests.println(requestString);
            requests.flush();
            return responses.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error reading server response.", e);
        }
    }

    @Override
    public JsonNode getResponse() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(responses.readLine(), JsonNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing server response as JSON.", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading server response.", e);
        }
    }

    @Override
    public void ping() {
        sendRequest("ping");
    }
}
