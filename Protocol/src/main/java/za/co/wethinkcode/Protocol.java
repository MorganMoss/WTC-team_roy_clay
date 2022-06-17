package za.co.wethinkcode;

import com.google.gson.Gson;

/**
 * Generic Protocol class that stores no data,
 * however it does allow classes that inherit it to be serialized easily
 */
public abstract class Protocol {
    /**
     * this function uses Google Gson
     * (a java data serialization package)
     * that takes this protocol and converts it into a String Json
     * @return a String Json
     */
    public String serialize(){
        return new Gson().toJson(this);
    }

    /**
     * this function uses Google Gson
     * (a javad ata serialization package)
     * Takes in a string Json and makes a Protocol object
     * @param json to be converted
     * @return a Protocol object
     */
    public static Protocol deSerialize(String json){
        return new Gson().fromJson(json,Request.class);
    }
}