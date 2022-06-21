package za.co.wethinkcode.server;

import java.io.*;
import java.net.Socket;
import za.co.wethinkcode.Request;
import za.co.wethinkcode.Response;

/**
 * This takes an established connection between the server and a client 
 * and handles communication between the two.
 */
public class ServerClientCommunicator extends Thread {

    private final DataInputStream requestIn;
    private final DataOutputStream responseOut;

    private final String clientMachine;

    /**
     * Constructor for a Server Client Communicator
     * @param socket The result of a connection to the server from the client.
     */
    public ServerClientCommunicator(Socket socket) throws IOException {
        clientMachine = socket.getInetAddress().getHostName();
        System.out.println("Connection from " + clientMachine);

        requestIn = new DataInputStream(socket.getInputStream());
        responseOut = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * Gets the request from the client and adds it to the server
     * @return true if still connected
     */
    private boolean passingRequest(){
        try {
            Request request = (Request) Request.deSerialize(requestIn.readUTF());
            
            //TODO: Can choose to add the robot name to a list here if it launches that robot.
            // To be able to remove those robots after a disconnection.
            // Could also use that fact to force the client to only reference new robot names with launch
            
            Server.addRequest(request);
        } catch (IOException clientDisconnected) {
            return false;
        }
        return true;
    }

    /**
     * Gets a response from the server and sends it to the client
     * @return true if still connected
     */
    private boolean passingResponse() {
        try {
            Response response = Server.getResponse("");
            
            if (response == null){
                return true;
            }
            
            responseOut.writeUTF(response.serialize());
            responseOut.flush();
        } catch (IOException clientDisconnected) {
            return false;
        }
        return true;
    }

    /**
     * To be run in a separate thread,
     * will handle I/O between Server and a Client
     */
    public void run(){
        while (true){
           if (!passingRequest() | !passingResponse()){
               break;
           }
        }
        
        //TODO: Take that list of robots and send a quit request for each.
        
        System.out.println(clientMachine + " has disconnected");
    }
}