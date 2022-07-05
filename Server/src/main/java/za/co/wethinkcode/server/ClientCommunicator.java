package za.co.wethinkcode.server;

import java.net.Socket;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import za.co.wethinkcode.Request;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.Handler;

/**
 * This takes an established connection between the server and a client 
 * and handles communication between the two.
 */
public final class ClientCommunicator {
    /**
     * Socket's inStream
     */
    private final BufferedReader requestIn;
    /**
     * Socket's outStream
     */
    private final PrintStream responseOut;
    /**
     * Launched robots are a collection of robot names that were part of a launch request.
     */
    private final Hashtable<String, Boolean> launchedRobots = new Hashtable<>();
    /**
     * Unlaunched robots are a collection of robot names that were part of a command,
     * but not a launch command. They are removed once they get a response
     */
    private final Hashtable<String, Integer> unLaunchedRobots = new Hashtable<>();
    /**
     * flag if a robot is launched more than once
     */
    private boolean duplicateLaunch = false;
    /**
     * flag to stop processes once the client disconnects
     */
    private boolean connected = true;

    private final String clientID = Integer.toHexString(this.hashCode());

    /**
     * Constructor for a Server Client Communicator
     * @param socket The result of a connection to the server from the client.
     */
    public ClientCommunicator(Socket socket) throws IOException {
        System.out.println("Connection from " + clientID + " with the address: " + socket.getInetAddress().getHostName());

        requestIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        responseOut = new PrintStream(socket.getOutputStream());

        AtomicLong latency = new AtomicLong();

        Thread requester = new Thread(
                () -> {
                    while (connected) {
                        passingRequest();
                    }
                }
        );

        Thread responder = new Thread(
                () -> {
                    while (connected) {
                        passingResponse();
                    }
                }
        );

        requester.start();
        responder.start();
    }

    /**
     * Handles the disconnection of the client,
     * will flag that they've disconnected
     * and purge all robots referenced by this client.
     */
    private void handleDisconnection(){
        connected = false;


        System.out.println(clientID + " has disconnected");

        for (String robot : launchedRobots.keySet()) {
            Server.purge(robot);
        }

    }

    /**
     * check that a request command is a launch
     * @param command from the request
     * @return true if launched, else false
     */
    private boolean isLaunchCommand(String command){
        return command.equalsIgnoreCase("launch");
    }

    /**
     * Adds a robot to the list of launched robots
     * @param robot from a new request
     */
    private void addLaunchedRobot(String robot){
        launchedRobots.put(robot, false);
    }

    /**
     * Checks if a robot has not been launched yet
     * @param robot from a new request
     * @return true if the robot has not been launched
     */
    private boolean isNewRobot(String robot){
            return !launchedRobots.contains(robot);
    }

    /**
     * Called when a robot in a request is not launched
     * and must be handled separately
     * @param robot that hasn't been launched
     */
    private void addUnLaunchedRobot(String robot){
        if (!unLaunchedRobots.contains(robot)){
            unLaunchedRobots.put(robot, 1);
            return;
        }

        int increment = unLaunchedRobots.get(robot);
        increment++;

        unLaunchedRobots.put(robot, increment);
    }

    /**
     * Gets the request from the client and adds it to the server
     */
    private void passingRequest(){
        String requestJSON = "";

        try {
            requestJSON = requestIn.readLine();
        } catch (IOException ignored) {}

        //null when socket is closed on the client side
        if (requestJSON == null){
            handleDisconnection();
            return;
        }

        Request request = Request.deSerialize(requestJSON);

        if (request == null){
            responseOut.println(Response.createError("Bad JSON format").serialize());
            responseOut.flush();
            return;
        }

        Handler.addRequest(clientID , request);

        handleNewAndLaunchedRobots(request);
    }

    /**
     * Handles the addition of new robots and prevents issues with repetitive launches.
     * @param request that just came in
     */
    private void handleNewAndLaunchedRobots(Request request) {
        String robot = request.getRobot();

        boolean newRobot = isNewRobot(robot);
        boolean launchCommand = isLaunchCommand(request.getCommand());

        if (launchCommand && newRobot){
            addLaunchedRobot(robot);
            return;
        }

        if (newRobot){
            addUnLaunchedRobot(robot);
            return;
        }

        if (launchCommand){
            duplicateLaunch = true;
        }
    }

    /**
     * Checks if a launch is successful,
     * ignores a failed launch if it is a duplicate for a robot.
     * @param response from the launch command
     * @return true if successful or duplicate launch
     */
    private boolean isSuccessfulLaunch(Response response){
        String message = (String) response.getData().getOrDefault("message", null);
        String result = response.getResult();

        if (duplicateLaunch){
            duplicateLaunch = false;
            return true;
        }

        if (message == null | result == null){
            return true;
        }

        return !(message.equalsIgnoreCase("No more space in this world") |
            message.equalsIgnoreCase("Internal error occurred")|
            message.equalsIgnoreCase("Too many of you in this world")|
            message.equalsIgnoreCase("Could not parse arguments"));
    }

    /**
     * Checks if the state of the robot is DEAD
     * @param response from a launched robot
     * @return true if the robot is dead and needs to be removed
     */
    private boolean isRobotDead(Response response){
        String status = (String) response.getData().getOrDefault("status", null);

        if (status == null){
            return false;
        }

        return status.equalsIgnoreCase("DEAD");
    }

    /**
     * Tries to remove the reference to an un launched robot,
     * if it has sent multiple requests,
     * it will instead note that it received a response for one of those requests
     * and not remove the reference.
     * Robots that are not in the list are ignored by this method.
     * @param robot that sent a request, but was never launched
     */
    private void tryRemoveUnLaunchedRobot(String robot){
        if (unLaunchedRobots.contains(robot)){
            int decrement = unLaunchedRobots.get(robot);

            if (decrement > 1) {
                decrement--;
                unLaunchedRobots.put(robot, decrement);
                return;
            }

            unLaunchedRobots.remove(robot);
            Server.purge(robot);
        }
    }

    /**
     * Collects the names of all launched and unlaunched robots
     * @return a set of all robot names
     */
    private HashSet<String> getAllRobots(){
        synchronized (launchedRobots) {
            synchronized (unLaunchedRobots) {
                return new HashSet<>() {{
                    addAll(launchedRobots.keySet());
                    addAll(unLaunchedRobots.keySet());
                }};
            }
        }
    }

    /**
     * Gets a response from the server and sends it to the client
     */
    private void passingResponse() {
        for (String robot : getAllRobots()){
            //tries to get responses for all robots simultaneously.
            Response response = Handler.getResponse(clientID , robot);

            if (response == null){
                continue;
            }

            try {
                if ((!isSuccessfulLaunch(response) && launchedRobots.get(robot))| isRobotDead(response)) {
                    launchedRobots.remove(robot);
                    Server.purge(robot);
                }

                if (!launchedRobots.get(robot)){
                    launchedRobots.put(robot, true);
                }
            } catch (NullPointerException ignored){}

            tryRemoveUnLaunchedRobot(robot);

            responseOut.println(response.serialize());
            responseOut.flush();

            System.out.println("Returning the response for " + clientID + "'s " + robot);
            System.out.println(response.serialize());
        }
    }
}