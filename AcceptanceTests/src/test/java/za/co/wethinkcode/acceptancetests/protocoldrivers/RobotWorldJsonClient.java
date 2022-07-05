package za.co.wethinkcode.acceptancetests.protocoldrivers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class RobotWorldJsonClient implements RobotWorldClient {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private Socket socket;
    private boolean connected = false;

    private JsonNode lastResponse = null;

    private BufferedReader responses;
    private PrintStream requests;

    public RobotWorldJsonClient(){
    }


    private void setupComms(){
        try {
            responses = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
            return;
        }

        setupComms();
    }


    @Override
    public void disconnect() {
        try {
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
    public JsonNode sendRequest(String requestString) {
        System.out.println(requestString);
        lastResponse = null;
        try {
            requests.println(requestString);
            requests.flush();
//            ObjectMapper mapper = new ObjectMapper();
            lastResponse = OBJECT_MAPPER.readTree(responses.readLine());
        } catch (IOException e) {
            throw new RuntimeException("Error reading server response.", e);
            //Q: what if the error was due to the response not being sent successfully?

        }
        return lastResponse;
    }


    public JsonNode sendRequest(String robot, String command, String args){
        String request = "{" +
                "\"robot\":\""+robot+"\"," +
                "\"command\":\""+command+"\"," +
                "\"arguments\":"+args +
                "}";
        return sendRequest(request);
    }

    public JsonNode sendRequest(String robot, String command){
        String request = "{" +
                "\"robot\":\""+robot+"\"," +
                "\"command\":\""+command+"\","+
                "\"arguments\":[]}";
        return sendRequest(request);
    }





    @Override
    public void ping() {
        sendRequest("ping");
    }


    @Override
    public boolean launchRobot() {
        return launchRobot("HAL");
    }

    @Override
    public JsonNode getResponse() {
        return lastResponse;
    }

    public boolean launchRobot(String name) {
        //Successfully launching a robot to the server
        JsonNode launch_response =  sendRequest(
                name, "launch", "[\"shooter\",\"5\",\"5\"]");

        System.out.println(launch_response);
        //Q: why do we want to print out this response?

        return (
            launch_response.get("result") != null
            && launch_response.get("result").asText().equalsIgnoreCase("OK")
        );
    }


    public void assertResult(JsonNode response, String status){
        assertNotNull(response.get("result"));
        assertEquals( status, response.get("result").asText() , response.asText());
    }


    public void assertMessage(JsonNode response, String message){
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
//        assertTrue(response.get("data").get("message").asText().contains(message));
    }


    public void assertPosition(JsonNode response, int x, int y){
        assertNotNull(response.get("state"));
        assertNotNull(response.get("state").get("position"));
        assertEquals(x, response.get("state").get("position").get(0).asInt());
        assertEquals(y, response.get("state").get("position").get(1).asInt());
    }

    @Override
    public int getX(JsonNode response) {
        return response.get("data").get("position").get(0).asInt();
    }

    @Override
    public int getY(JsonNode response) {
        return response.get("data").get("position").get(1).asInt();
    }

}
