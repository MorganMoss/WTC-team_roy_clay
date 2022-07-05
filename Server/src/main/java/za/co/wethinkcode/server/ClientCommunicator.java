package za.co.wethinkcode.server;

import java.io.*;
import java.net.Socket;
import java.util.*;

import za.co.wethinkcode.Request;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.Handler;

/**
 * This takes an established connection between the server and a client 
 * and handles communication between the two.
 */
public final class ClientCommunicator {

    /**
     * Using a synchronizedSet,
     * as there should only be one of each robot name in this collection,
     * and it is used in multiple threads.
     * Having it synchronized makes it threadsafe.
     */
    private final BufferedReader requestIn;
    private final PrintStream responseOut;
    private final String clientMachine;

    private final Set<String> launchedRobots = Collections.synchronizedSet(new HashSet<>());
//    private final List<String> unLaunchedRobots = Collections.synchronizedList(new ArrayList<>());
    private final Hashtable<String, Integer> unLaunchedRobots = new Hashtable<>();

    private boolean duplicateLaunch = false;
    private boolean connected = true;

    private final Thread responder;
    private final Thread requester;


    /**
     * Constructor for a Server Client Communicator
     * @param socket The result of a connection to the server from the client.
     */
    public ClientCommunicator(Socket socket) throws IOException {
        clientMachine = socket.getInetAddress().getHostName();
        System.out.println("Connection from " + clientMachine);

        requestIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        responseOut = new PrintStream(socket.getOutputStream());

        requester = new Thread(
                () -> {
                    while (connected) {
                        if (!passingRequest()) {
                            break;
                        }

                    }
                    System.out.println(clientMachine + " has disconnected");

                    for (String robot : launchedRobots) {
                        Server.purge(robot);
                    }

                    connected = false;
                }
        );

       responder = new Thread(
                () -> {
                    while (connected) {
                        if (!passingResponse()) {
                            break;
                        }
                    }
                }
        );

        requester.start();
        responder.start();
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
        launchedRobots.add(robot);
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
     * @return true if still connected
     */
    private boolean passingRequest(){
        try {
            String requestJSON = requestIn.readLine();

            //null when socket is closed on the client side
            if (requestJSON == null){
                return false;
            }

            Request request = Request.deSerialize(requestJSON);

            if (request == null){
                responseOut.println(Response.createError("Bad JSON format").serialize());
                responseOut.flush();
                return true;
            }

            boolean newRobot = isNewRobot(request.getRobot());
            boolean launchCommand = isLaunchCommand(request.getCommand());

            if ( newRobot ){
                if (launchCommand){
                    addLaunchedRobot(request.getRobot());
                } else {
                    addUnLaunchedRobot(request.getRobot());
                }
            } else {
                if (launchCommand){
                    //this robot exists already, we can duplicateLaunch this when we check the response later
                    duplicateLaunch = true;
                }
            }
            
            Handler.addRequest(this.toString() , request);

        } catch (IOException clientDisconnected) {
            return false;
        }
        return true;
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
        message.equalsIgnoreCase("Too many of you in this world")|
        result.equalsIgnoreCase("ERROR"));
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
        }
    }

    /**
     * Gets a response from the server and sends it to the client
     * @return true if still connected
     */
    private boolean passingResponse() {
        HashSet<String> robots = new HashSet<>();

        synchronized (launchedRobots) {
            synchronized (unLaunchedRobots) {
                robots = new HashSet<>() {{
                    addAll(launchedRobots);
                    addAll(unLaunchedRobots.keySet());
                }};
            }
        }


        for (String robot : robots){
            //tries to get responses for all robots simultaneously.
            Response response = Handler.getResponse(this.toString(), robot);

            if (response == null){
                continue;
            }


            if (!isSuccessfulLaunch(response) | isRobotDead(response)) {
                launchedRobots.remove(robot);
            }

            tryRemoveUnLaunchedRobot(robot);


            responseOut.println(response.serialize());
            responseOut.flush();

            System.out.println("Returning the response for " + this.toString() + "'s " + robot);
            System.out.println(response.serialize());


        }


        return !responseOut.checkError();
    }
}