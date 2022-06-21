package za.co.wethinkcode;

import com.google.gson.Gson;

import java.util.List;

/**
 * The request protocol should be made by a client and sent to the server for processing
 */
public class Request extends Protocol{
    private final String robot, command;
    private final List<?> arguments;

    /**
     * Constructor for the Request Protocol
     * @param robot name of the robot the client controls
     * @param command name of the command the client wants to execute
     * @param arguments the list of data that command requires
     */
    public Request (String robot, String command, List<?> arguments){
        this.robot = robot;
        this.command = command;
        this.arguments = arguments;
    }

    public String getRobot() {
        return robot;
    }

    public String getCommand() {
        return command;
    }

    public List<?> getArguments() {
        return arguments;
    }

    /**
     * this function uses Google Gson
     * (a java data serialization package)
     * Takes in a string Json and makes a Request object
     * @param json to be converted
     * @return a Request object
     */
    public static Request deSerialize(String json){
        return new Gson().fromJson(json, Request.class);
    }
}
