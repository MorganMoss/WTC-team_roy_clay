package za.co.wethinkcode.server.handler;

import za.co.wethinkcode.Request;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.command.Command;
import za.co.wethinkcode.server.handler.command.CouldNotParseArgumentsException;
import za.co.wethinkcode.server.handler.command.RobotNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.javatuples.Pair;


/**
 * The handler class is a static class that
 * takes requests, executes them as commands
 * and returns responses.
 */
public final class Handler extends Thread{

    /**
     * Pre-formatted Response
     * for a robot that does not exist
     */
    private static final Response ROBOT_DOES_NOT_EXIST = Response.createError("Robot does not exist");

    /**
     * Pre-formatted Response
     * for an unsupported command
     */
    private static final Response UNSUPPORTED_COMMAND = Response.createError("Unsupported command");
    /**
     * Pre-formatted Response
     * for arguments a command could not parse
     */
    private static final Response COULD_NOT_PARSE_ARGUMENTS = Response.createError("Could not parse arguments");
    /**
     * Pre-formatted Response
     * for an internal error
     */
    private static final Response INTERNAL_ERROR = Response.createError("Internal error occurred");

    private static final Queue<Pair<String, Request>> requests = new PriorityQueue<>();
    private static final Hashtable<Pair<String, String>, Response> responses = new Hashtable<>();

    /**
     * Adds a request specific to a client to a queue,
     * i.e. first in first out
     * @param client that called this function
     * @param request the request the client sent
     */
    public static void addRequest(String client, Request request){
        requests.add(new Pair<>(client, request));
    }

    /**
     * Takes a request, uses commands to create a response
     * @param request given from server
     * @return a response from the command executed, or an error response
     */
    public static Response executeRequest(Request request){
        if (request == null)
            return INTERNAL_ERROR;

        if (request.getRobot() == null
            | request.getCommand() == null
            | request.getArguments() == null){
            return INTERNAL_ERROR;
        }

        Command command = createCommand(request.getCommand());

        if (command == null){
            return UNSUPPORTED_COMMAND;
        }

        try {
            command.setRobot(request.getRobot());
        } catch (RobotNotFoundException robotDoesNotExist){
            return ROBOT_DOES_NOT_EXIST;
        }

        try {
            command.setArguments(request.getArguments());
        } catch (CouldNotParseArgumentsException badArguments){
            return COULD_NOT_PARSE_ARGUMENTS;
        }

        try {
            return command.execute();
        } catch (Exception internalError){
            return INTERNAL_ERROR;
        }
    }

    /**
     * Gets a response specific to the client
     * and the robot that was responsible for the request.
     * Will hang until a response is found.
     * @param client that sent the request
     * @param robot (name) part of that request
     * @return the latest response specific to those two
     */
    public static Response getResponse(String client, String robot){
        Response response = null;
        while (response == null){
            response = responses.getOrDefault(new Pair<>(client, robot), null);
        }
        return response;
    }

    /**
     * Makes the command name match that of the class it represents.
     * @param command unformatted
     * @return the command name matching that of the class it represents.
     */
    private static String formatCommandName(String command){
        return "za.co.wethinkcode.server.handler.command."
                + command.substring(0, 1).toUpperCase()
                + command.substring(1).toLowerCase()
                + "Command";
    }

    /**
     * This will take a request and dynamically create
     * a new command object based on the command name
     * @param command given by the request
     * @return command object representing the request
     */
    private static Command createCommand(String command) {
        try {
            Class<?> loadedCommand = Class.forName(formatCommandName(command));
            return (Command) loadedCommand.getDeclaredConstructor().newInstance();

        } catch (LinkageError | ClassNotFoundException notFound){
            return null;

        } catch (InstantiationException
                 | InvocationTargetException
                 | IllegalAccessException
                 | NoSuchMethodException notInstantiated) {

            throw new RuntimeException(notInstantiated);
        }
    }


    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true){
            //pop the queue
            Pair<String, Request> requestFromClient;
            try {
               requestFromClient = requests.remove();
            } catch (NoSuchElementException emptyQueue) {
                continue;
            }
            //execute that request
            Response response = executeRequest(requestFromClient.getValue1());
            //associate the response from that execution with the client
            Pair<String, String> clientAssociatedWithRobot = new Pair<>(
                    requestFromClient.getValue0(),
                    requestFromClient.getValue1().getRobot()
            );
            //add that response to the hashmap
            responses.put(clientAssociatedWithRobot, response);
        }
    }

    public static synchronized void setup() {
        new Handler().start();
    }

    /**
     * The constructor must never be used outside this class, as it is treated as static
     */
    private Handler(){}
}
//TODO: Remove this
//    /**
//     * FOR TESTING PURPOSES
//     */
//    public static void main(String[] args) {
//        Scanner in = new Scanner(System.in);
//        while (true) {
//            try {
//                Command c = createCommand(in.nextLine());
//                System.out.println(c.getClass().toString());
//            } catch (RuntimeException e) {
//                System.out.println(e);
//            }
//        }
//    }

