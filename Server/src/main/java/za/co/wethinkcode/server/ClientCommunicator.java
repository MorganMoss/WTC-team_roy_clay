package za.co.wethinkcode.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import za.co.wethinkcode.Request;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.Handler;

/**
 * This takes an established connection between the server and a client 
 * and handles communication between the two.
 */
public final class ClientCommunicator {

    private final List<String> robots = new ArrayList<>();

    private final BufferedReader requestIn;
    private final PrintStream responseOut;


    private final String clientMachine;

    private boolean duplicateLaunch = false;

    /**
     * Constructor for a Server Client Communicator
     * @param socket The result of a connection to the server from the client.
     */
    public ClientCommunicator(Socket socket) throws IOException {
        clientMachine = socket.getInetAddress().getHostName();
        System.out.println("Connection from " + clientMachine);

        requestIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        responseOut = new PrintStream(socket.getOutputStream());

        Thread responder = new Thread(
                () -> {
                    while (true) {
                        if (!passingResponse()) {
                            break;
                        }
                    }
                    System.out.println(clientMachine + " has disconnected");
                }
        );
        responder.start();

        //TODO: Take that list of robots and send a quit request for each.
        Thread requester = new Thread(
                () -> {
                    while (true) {
                        if (!passingRequest()) {
                            break;
                        }
                    }
                    //TODO: Take that list of robots and send a quit request for each.
                }
        );
        requester.start();
    }

    /**
     * check that a request command is a launch
     * @param request from the client
     * @return true if launched, else false
     */
    private boolean isLaunch(Request request){
        return request.getCommand().equalsIgnoreCase("launch");
    }

    private void addRobot(String robot){
        if (robots.contains(robot)){
            //this robot exists already, we can duplicateLaunch this when we check the response later
            duplicateLaunch = true;
            return;
        }
        robots.add(robot);
    }

    /**
     * Gets the request from the client and adds it to the server
     * @return true if still connected
     */
    private boolean passingRequest(){
        try {
            String requestJSON = requestIn.readLine();
            Request request = Request.deSerialize(requestJSON);

            if (request == null){
                responseOut.println(Response.createError("Bad JSON format").serialize());
                responseOut.flush();
                return true;
            }

            if(isLaunch(request)){
                addRobot(request.getRobot());
            }
            
            Handler.addRequest(this.toString() , request);
        } catch (IOException clientDisconnected) {
            return false;
        }
        return true;
    }

    private boolean isSuccessfulLaunch(Response response){
        String message = (String) response.getData().getOrDefault("message", null);

        if (duplicateLaunch){
            duplicateLaunch = false;
            return true;
        }

        return !(message.equalsIgnoreCase("No more space in this world") |
        message.equalsIgnoreCase("Too many of you in this world"));
    }

    private boolean isRobotDead(Response response){
        String status = (String) response.getData().getOrDefault("status", null);

        return status.equalsIgnoreCase("DEAD");
    }

    private void removeRobot(String robot){

        robots.remove(robot);
    }

    /**
     * Gets a response from the server and sends it to the client
     * @return true if still connected
     */
    private boolean passingResponse() {
        for (String robot : robots) {
            Response response = Handler.getResponse(this.toString(), robot);

            if (!isSuccessfulLaunch(response) | isRobotDead(response)) {
                removeRobot(robot);
            }

            responseOut.println(response.serialize());
            responseOut.flush();

        }
        return responseOut.checkError();
    }
}