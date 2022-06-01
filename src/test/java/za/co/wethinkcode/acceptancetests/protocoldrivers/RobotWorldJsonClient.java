package za.co.wethinkcode.acceptancetests.protocoldrivers;

import com.fasterxml.jackson.databind.JsonNode;

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
        } catch (IOException ignored) {
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
    public JsonNode getResponse() {
        try {
            return responses.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void ping() {
        sendRequest("ping");
    }
}
