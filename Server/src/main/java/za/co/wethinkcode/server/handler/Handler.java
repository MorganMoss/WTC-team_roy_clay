package za.co.wethinkcode.server.handler;

import za.co.wethinkcode.Request;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.command.Command;
import za.co.wethinkcode.server.handler.command.CouldNotParseArgumentsException;
import za.co.wethinkcode.server.handler.command.RobotNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * The handler class takes requests, executes them as commands and returns responses.
 */
public class Handler {
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

    /**
     * Takes a request, uses commands to create a response
     * @param request given from server
     * @return a response from the command executed, or an error response
     */
    public static Response executeRequest(Request request){
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

