package za.co.wethinkcode.server.handler;

import za.co.wethinkcode.Request;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.command.Command;
import za.co.wethinkcode.server.handler.command.CouldNotParseArgumentsException;
import za.co.wethinkcode.server.handler.command.RobotNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Handler {
    /**
     * Pre-formatted Response
     * for a robot that does not exist
     */
    private static final Response ROBOT_DOES_NOT_EXIST = new Response(
            "ERROR",
            new HashMap<>(){{
                put("message", "Robot does not exist");
            }});

    /**
     * Pre-formatted Response
     * for an unsupported command
     */
    private static final Response UNSUPPORTED_COMMAND = new Response(
            "ERROR",
            new HashMap<>(){{
        put("message", "Unsupported command");
    }});

    /**
     * Pre-formatted Response
     * for arguments a command could not parse
     */
    private static final Response COULD_NOT_PARSE_ARGUMENTS = new Response(
            "ERROR",
            new HashMap<>(){{
                put("message", "Could not parse arguments");
            }});

    /**
     * Pre-formatted Response
     * for an internal error
     */
    private static final Response INTERNAL_ERROR = new Response(
            "ERROR",
            new HashMap<>(){{
                put("message", "An internal error has occurred");
            }});

    private static final Module module = Handler.class.getModule();


    /**
     * Takes a request, uses commands to create a response
     * @param request given from server
     * @return a response from the command executed, or a error response
     */
    public static Response executeRequest(Request request){
        Response response;
        Command command = createCommand(request.getCommand());

        if (command == null){
            return UNSUPPORTED_COMMAND;
        }

        try {
            command.setRobot(request.getRobot());
        } catch (RobotNotFoundException e){
            return ROBOT_DOES_NOT_EXIST;
        }

        try {
            command.setArguments(request.getArguments());
        } catch (CouldNotParseArgumentsException e){
            return COULD_NOT_PARSE_ARGUMENTS;
        }

        response = command.execute();

        return response;
    }

    /**
     * Makes the command name match that of the class it represents.
     * @param command unformatted
     * @return the command name matching that of the class it represents.
     */
    private static String formatCommandName(String command){
        return command.substring(0, 1).toUpperCase()
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
            Class<?> loadedCommand = Class.forName(module, formatCommandName(command));
            return (Command) loadedCommand.getDeclaredConstructor().newInstance();

        } catch (LinkageError notFound){
            return null;

        } catch (InstantiationException
             | InvocationTargetException
             | IllegalAccessException
             | NoSuchMethodException notInstantiated) {

            throw new RuntimeException(notInstantiated);
        }
    }
}

