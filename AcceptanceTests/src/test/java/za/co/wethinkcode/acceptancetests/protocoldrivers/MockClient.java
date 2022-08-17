package za.co.wethinkcode.acceptancetests.protocoldrivers;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.*;

import za.co.wethinkcode.Protocol.*;
import za.co.wethinkcode.Request;
import za.co.wethinkcode.Response;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A basic interface that wraps the expected Robot Worlds API so that we can easily connect to and send requests to the server.
 */
public class MockClient {
    private Socket socket;
    private boolean connected = false;
    private BufferedReader responses;
    private PrintStream requests;
    private Response lastResponse;

    public MockClient(){
    }


    private void setupComms(){
        try {
            responses = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            requests = new PrintStream(socket.getOutputStream());
        } catch (IOException ignored) {
        }
    }

    /**
     * Connects to the Robot Worlds server on specified ip address and port
     * @param IPAddress either `localhost` or actual IP address
     * @param port port that server is configured to receive connections on
     */
    public void connect(String IPAddress, int port) {
        for (int i = 10; i > 0; i--){
            try {
                this.socket = new Socket(IPAddress, port);
                connected = true;
                setupComms();
                return;
            } catch (IOException notConnected) {
                connected = false;
                System.out.println("Failed to connect. Trying again . . .");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Disconnect from server
     */
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException | NullPointerException e) {
            //error connecting should just throw Runtime error and fail test
//            throw new RuntimeException("Error disconnecting from Robot Worlds server.", e);
        }
        connected = false;
    }

    /**
     * Checks if client is connected to server
     * @return true if connected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Sends a request presented by the Json object to the server
     * @param requestString a String representing the Json string to send to server
     * @return the response
     */
    public Response sendRequest(String requestString) {
        lastResponse = null;
        try {
            requests.println(requestString);
            requests.flush();
            lastResponse = Response.deSerialize(responses.readLine());
        } catch (IOException e) {
            throw new RuntimeException("Error reading server response.", e);
            //Q: what if the error was due to the response not being sent successfully?

        }
        return lastResponse;
    }

    /**
     * Sends a request presented by the Json object to the server
     * @param robot name
     * @param command requested to be run
     * @param args of that command
     * @return the response
     */
    public Response sendRequest(String robot, String command, String args){
        String request = "{" +
                "\"robot\":\""+robot+"\"," +
                "\"command\":\""+command+"\"," +
                "\"arguments\":"+args +
                "}";
        return sendRequest(request);
    }

    /**
     * Sends a request presented by the Json object to the server
     * @param robot name
     * @param command requested to be run
     * @return the response
     */
    public Response sendRequest(String robot, String command){
        String request = "{" +
                "\"robot\":\""+robot+"\"," +
                "\"command\":\""+command+"\","+
                "\"arguments\":[]}";
        return sendRequest(request);
    }

    /**
     * Sends a garbage input to see if the server receives it.
     */
    public void ping() {
        sendRequest("ping");
    }

    public boolean launchRobot() {
        return launchRobot("HAL");
    }

    public Response getResponse() {
        return lastResponse;
    }

    /**
     * Quick launch request
     * @param name of robot to be launched
     * @return true if launch was successful
     */
    public boolean launchRobot(String name) {
        //Successfully launching a robot to the server
        Response launch_response =  sendRequest(
                name, "launch", "[\"shooter\",\"5\",\"5\"]");

        return (
            launch_response.getResult() != null
            && launch_response.getResult().equalsIgnoreCase("OK")
        );
    }

    public void assertResult(Response response, String status){
        assertNotNull(response.getResult());
        assertEquals( status, response.getResult() , response.toString());
    }


    public void assertMessage(Response response, String message){
        assertNotNull(response.getData());
        assertNotNull(response.getData().get("message"));
        assertEquals(message, response.getData().get("message"));
    }

    public Point getPosition(HashMap<String, ?> state){
        ArrayList<Double> position = (ArrayList<Double>) state.get("position");
        return new Point((int) Math.round(position.get(0)), (int) Math.round(position.get(1)));

    }


    public void assertPosition(Response response, int x, int y){
        assertNotNull(response.getState());
        assertNotNull(response.getState().get("position"));
        Point position = getPosition(response.getState());
        assertEquals(x, position.x);
        assertEquals(y, position.y);
    }
    public int getAsInt(Map<String, Object> data, String key){
        return (int) Math.round((Double) data.get(key));
    }

    public ArrayList<Map<String, Object>> getObjects(Response response){
        ArrayList<Map<String, Object>> objects = (ArrayList<Map<String, Object>>) response.getData().get("objects");
        return objects;
    }

}
