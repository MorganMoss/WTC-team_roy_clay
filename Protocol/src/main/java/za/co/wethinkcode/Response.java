package za.co.wethinkcode;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

/**
 * The response protocol should be made by the server and sent to the client for processing
 */
public class Response extends Protocol{

    private final String result;
    private final HashMap<String, ?> data;
    private HashMap<String, ?> state = null;

    /**
     * Constructor for the Response Protocol.
     * Neglects state, must be added at a later stage
     * @param result from the command
     * @param data to expand on the result of the command executed
     */
    public Response(String result, HashMap<String, ?> data){
        this.result = result;
        this.data = data;
    }

    /**
     * To add the optional state for the Response
     * @param state of the robot after the execution of some commands
     */
    public void addState(HashMap<String,?> state){
        this.state = state;
    }

    public String getResult() {
        return result;
    }

    public HashMap<String, ?> getData() {
        return data;
    }

    public HashMap<String, ?> getState() {
        return state;
    }

    /**
     * this function uses Google Gson
     * (a java data serialization package)
     * Takes in a string Json and makes a Response object
     * @param json to be converted
     * @return a Response object
     */
    public static Response deSerialize(String json){
        try {
            return new Gson().fromJson(json, Response.class);
        } catch (
        JsonSyntaxException badJSON){
            return null;
        }
    }

    /**
     * Used to make error messages
     * @param message The message passed with the error
     * @return Error Response
     */
    public static Response createError(String message){
        return new Response("ERROR", new HashMap<>(){{put("message", message);}});
    }

    /**
     * Used to make success messages
     * @param data The data given by a commands execution
     * @return OK Response
     */
    public static Response createOK(HashMap<String, ?> data){
        return new Response("OK", data);
    }

    /**
     * Used to make success messages
     * @param message The message passed with the response
     * @return OK Response
     */
    public static Response createOK(String message){
        return new Response("OK", new HashMap<>(){{put("message", message);}});
    }

    /**
     * Used to make success messages
     * @return OK Response
     */
    public static Response createOK(){
        return createOK(new HashMap<>());
    }
}
