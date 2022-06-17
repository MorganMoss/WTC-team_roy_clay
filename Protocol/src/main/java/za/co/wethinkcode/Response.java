package za.co.wethinkcode;

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



}
